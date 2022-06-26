package org.noah.core.generator;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

public class EnhanceFreemarkerTemplateEngine extends FreemarkerTemplateEngine {

    private Configuration configuration;

    @SneakyThrows
    @Override
    public FreemarkerTemplateEngine init(ConfigBuilder configBuilder) {
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding(ConstVal.UTF8);
        configuration.setDirectoryForTemplateLoading(new File(System.getProperty("user.dir")+"/templates"));
//        configuration.setClassForTemplateLoading(EnhanceFreemarkerTemplateEngine.class, StringPool.SLASH);
        return this;
    }


    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, File outputFile) throws Exception {
        Template template = configuration.getTemplate(templatePath);
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            template.process(objectMap, new OutputStreamWriter(fileOutputStream, ConstVal.UTF8));
        }
    }


    @Override
    public String templateFilePath(String filePath) {
        return filePath;
    }

    @Override
    protected void outputCustomFile(Map<String, String> customFile,TableInfo tableInfo,Map<String, Object> objectMap) {
        String entityName = tableInfo.getEntityName();
        String otherPath = this.getPathInfo(OutputFile.other);
        customFile.forEach((key, value) -> {
            String fileName = String.format(otherPath + File.separator + entityName + "%s", key);
            if("List.vue".equals(key) || "Form.vue".equals(key)){
                fileName = String.format(CodeGenerator.VUE_ROOT + CodeGenerator.VUE_VIEW_PATH
                        + File.separator + entityName + "%s", key);
            }
            if("index.js".equals(key)){
                fileName = String.format(CodeGenerator.VUE_ROOT + CodeGenerator.VUE_VIEW_PATH
                        + File.separator + "%s", key);
            }
            this.outputFile(new File(fileName), objectMap, value);
        });
    }
}