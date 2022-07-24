package org.noah.flowable.service.impl;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BaseElement;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.ExtensionAttribute;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.image.ProcessDiagramGenerator;
import org.noah.core.common.PageResult;
import org.noah.core.utils.BeanUtils;
import org.noah.flowable.pojo.model.FlwModelPage;
import org.noah.flowable.pojo.model.ModelSaveVO;
import org.noah.flowable.pojo.model.ModelVO;
import org.noah.flowable.service.IFlwModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * 工作流模型设计Service实现
 *
 * @author Noah
 * @since 2022-07-23
 */
@Service
public class FlwModelService implements IFlwModelService {

    @Autowired
    RepositoryService repositoryService;

    @Override
    public ModelVO getById(String id) {
        Model model = repositoryService.getModel(id);
        ModelVO vo = BeanUtils.copyObject(model, ModelVO.class);
        if (model != null && model.hasEditorSource()) {
            byte[] source = this.repositoryService.getModelEditorSource(model.getId());
            vo.setEditor(new String(source, StandardCharsets.UTF_8));
        }
        return vo;
    }

    @Override
    public PageResult<ModelVO> selectPage(FlwModelPage page) {
        PageResult<ModelVO> result = new PageResult<>();
        //创建查询实例
        ModelQuery query = repositoryService.createModelQuery();
        //查询条件
        if(StringUtils.isNotBlank(page.getName())){
            query.modelNameLike(page.getName());
        }
        if (StringUtils.isNotBlank(page.getKey())) {
            query.modelKey(page.getKey());
        }
        if (StringUtils.isNotBlank(page.getCategory())) {
            query.modelCategory(page.getCategory());
        }
        //执行分页查询并返回结果
        List<Model> list = query.orderByCreateTime().desc().listPage((int) ((page.getCurrent()-1)*page.getSize()), (int)page.getSize());
        List<ModelVO> voList = BeanUtils.copyList(list, ModelVO.class);
        result.setRecords(voList);
        result.setTotal(query.count());
        return result;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean deleteModel(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new FlowableException("请选择需删除流程");
        } else {
            Model model = (Model)this.repositoryService.createModelQuery().modelId(id).singleResult();
            if (!StringUtils.isEmpty(model.getDeploymentId())) {
                throw new FlowableException("流程已发布，无法删除");
            } else {
                this.repositoryService.deleteModel(id);
                return true;
            }
        }
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean deployModel(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new FlowableException("请选择需发布流程");
        } else {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();;
            ProcessEngineConfiguration configuration = processEngine.getProcessEngineConfiguration();
            Model model = repositoryService.getModel(id);
            if (model == null) {
                throw new FlowableObjectNotFoundException("找不到id为 '" + id + "'模型", Model.class);
            } else if (model.getDeploymentId() != null && model.getDeploymentId().length() > 0) {
                throw new FlowableException("当前模型已经发布，不可删除！");
            } else if (!model.hasEditorSource()) {
                throw new FlowableObjectNotFoundException("id 为'" + id + "' 的模型没有可用的资源。", String.class);
            } else {
                byte[] bpmnBytes = repositoryService.getModelEditorSource(id);
                String fileName = model.getId() + ".bpmn20.xml";
                Deployment deploy = repositoryService.createDeployment().name(model.getName()).category(model.getCategory()).tenantId(model.getTenantId()).addBytes(fileName, bpmnBytes).deploy();
                if (deploy != null) {
                    model.setDeploymentId(deploy.getId());
                    repositoryService.saveModel(model);
                    ProcessDefinition processDef = this.repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
                    this.repositoryService.setProcessDefinitionCategory(processDef.getId(), deploy.getCategory());
                }else{
                    throw new FlowableException("模型发布失败！");
                }
            }
            return true;
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public String save(ModelSaveVO vo) {
        if (vo.getEditor() != null && vo.getEditor().length() != 0) {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();;
            ProcessEngineConfiguration configuration = processEngine.getProcessEngineConfiguration();
            return this.saveModel(configuration, vo, null);
        } else {
            throw new FlowableException("保存模型失败，模型内容不能为空");
        }
    }
    private String getAttributeValue(BaseElement element, String name) {
        List<ExtensionAttribute> extensionAttributes = element.getAttributes().get(name);
        return !CollectionUtils.isEmpty(extensionAttributes) ? ((ExtensionAttribute)extensionAttributes.get(0)).getValue() : null;
    }
    private String saveModel(ProcessEngineConfiguration configuration, ModelSaveVO vo, String tenantId) {
        String id = vo.getId();
        byte[] editor = vo.getEditor().getBytes(StandardCharsets.UTF_8);;
        BpmnModel bpmnModel = this.generateBpmnModel(editor);
        String key = bpmnModel.getMainProcess().getId();
        String name = bpmnModel.getMainProcess().getName();
        String category = this.getAttributeValue(bpmnModel.getMainProcess(), "category");
        String description = bpmnModel.getMainProcess().getDocumentation();
        ModelQuery modelQuery = repositoryService.createModelQuery().latestVersion().modelKey(key);
        if (tenantId != null && tenantId.length() > 0) {
            modelQuery.modelTenantId(tenantId);
        }

        Model model = (Model)modelQuery.singleResult();
        if (StringUtils.isNotEmpty(id) && !model.getKey().equals(key)) {
            throw new FlowableException("模型保存失败: ProcessId Is Changed");
        } else {
            int version = model == null ? 1 : model.getVersion();
            if (model == null) {
                model = repositoryService.newModel();
            } else if (model.getDeploymentId() != null && model.getDeploymentId().length() > 0) {
                model = repositoryService.newModel();
                ++version;
            }

            model.setKey(key);
            model.setName(name);
            model.setCategory(category);
            model.setMetaInfo(description);
            model.setVersion(version);
            if (tenantId != null && tenantId.length() > 0) {
                model.setTenantId(tenantId);
            }

            repositoryService.saveModel(model);

            try {
                this.addSourceAndSourceExtra(configuration, repositoryService, editor, bpmnModel, model.getId());
            } catch (IOException var12) {
                throw new FlowableException("SaveModelEditorCmd error: ", var12);
            }

            return model.getId();
        }
    }

    private BpmnModel generateBpmnModel(byte[] bytes) {
        try {
            XMLInputFactory xif = XMLInputFactory.newInstance();
            InputStreamReader xmlIn = new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8);
            XMLStreamReader xtr = xif.createXMLStreamReader(xmlIn);
            BpmnModel bpmnModel = (new BpmnXMLConverter()).convertToBpmnModel(xtr);
            return bpmnModel;
        } catch (Exception var6) {
            throw new FlowableException("SaveModelEditorCmd error: " + var6.getMessage());
        }
    }

    private void addSourceAndSourceExtra(ProcessEngineConfiguration configuration, RepositoryService repositoryService, byte[] bytes, BpmnModel bpmnModel, String modelId) throws IOException {
        if (bytes != null) {
            repositoryService.addModelEditorSource(modelId, bytes);
            ProcessDiagramGenerator diagramGenerator = configuration.getProcessDiagramGenerator();
            InputStream resource = diagramGenerator.generateDiagram(bpmnModel, "png", Collections.emptyList(), Collections.emptyList(), configuration.getActivityFontName(), configuration.getLabelFontName(), configuration.getAnnotationFontName(), configuration.getClassLoader(), 1.0D, true);
            repositoryService.addModelEditorSourceExtra(modelId, IOUtils.toByteArray(resource));
        }

    }

}
