package org.noah.core.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.noah.core.common.BaseController;
import org.noah.core.common.BaseMapper;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 自定义MP代码生成器
 * @author Noah
 */
public class CodeGenerator {
    private static final String a = "sys"; //前缀（大模块分类）
    private static final String b = "org"; //业务
    private static final String TABLE = "SYS_ORG"; //表名（大写）
    private static final String TABLE_DESC = "组织"; //表备注
    private static final String API_TAG = "组织管理";  //接口文档名称
    private static final String LOG_PREFIX = "系统管理/组织管理";  //日志前缀
    private static final int API_SORT = 91; //接口文档排序

    private static final String PREFIX = ""; //表名称前缀
    private static final String AUTHOR = "Noah"; //作者
//    private static final String ROOT_PATH = "/module/file"; //项目模块
//    private static final String MODULE_PARENT = "org.noah"; //包名
//    private static final String MODULE = "file"; //子包名
    private static final String ROOT_PATH = "/admin";  //项目模块
    private static final String MODULE = a; //子包名
    private static final String MODULE_PARENT = "org.noah.web"; //包名
    private static final String POJO_PACKAGE = b;  //存放pojo的子包名
    private static final String PERMISSION_PREFIX = a+"-"+b;  //权限标识前缀
    private static final String REQUEST_MAPPING_PREFIX = "/"+ a + "/" + b;  //请求路径前缀
    public static final Boolean HAS_VUE = true; //是否生成前端VUE代码
    public static final String VUE_ROOT = "D:/work/noah/noah-vue"; //vue项目根路径
    public static final String VUE_VIEW_PATH = "/src/views/admin" + REQUEST_MAPPING_PREFIX; //生成模块的试图路径

    public static void main(String[] args) {
        DataSourceConfig.Builder dscBuilder = new DataSourceConfig
                .Builder("jdbc:mysql://192.168.64.129:3306/frame?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai",
                "frame", "EbR2BG6KxtYKRZRS");
        Map<OutputFile, String> pathInfo = new HashMap<>();
        //程序当前路径
        String projectPath = System.getProperty("user.dir") + ROOT_PATH;
        String modulePath = projectPath +"/src/main/java/"+MODULE_PARENT.replace(".","/")+"/" + MODULE;
        pathInfo.put(OutputFile.entity, modulePath + "/pojo/"+POJO_PACKAGE);
        pathInfo.put(OutputFile.other, modulePath + "/pojo/"+POJO_PACKAGE);

        String[] TABLE_NAME = {TABLE}; //表名（大写）
        Map<String, Object> param = new HashMap<>();
        param.put("permission", PERMISSION_PREFIX);
        param.put("tableDesc", TABLE_DESC);
        param.put("apiTag", API_TAG);
        param.put("apiSort", API_SORT);
        param.put("logPrefix", LOG_PREFIX);
        param.put("requestMapping", REQUEST_MAPPING_PREFIX);
        Map<String, String> file = new HashMap<>();
        file.put("ListVO.java", "/entityListVO.java.ftl");
        file.put("VO.java", "/entityVO.java.ftl");
        file.put("Page.java", "/entityPage.java.ftl");
        if(HAS_VUE){
            file.put("List.vue", "/entityList.vue.ftl");
            file.put("Form.vue", "/entityForm.vue.ftl");
            file.put("index.js", "/index.js.ftl");
        }
        FastAutoGenerator.create(dscBuilder)
                // 全局配置
                .globalConfig((builder) -> builder.author(AUTHOR) //.fileOverride()
                        .outputDir(projectPath +"/src/main/java")
//                        .enableSwagger()
                        .disableOpenDir())
                // 包配置
                .packageConfig((builder) -> builder.parent(MODULE_PARENT).moduleName(MODULE)
                        .entity("pojo."+POJO_PACKAGE)
                        .other("pojo."+POJO_PACKAGE)
                        .pathInfo(pathInfo))
                .injectionConfig((builder) -> builder.beforeOutputFile((tableInfo, objectMap) -> {
                        System.out.println("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size());
                    })
                        .customMap(param)
                        .customFile(file)
                ).templateConfig(builder -> {
                    builder.entity("/entity.java.ftl")
                            .service("/service.java.ftl")
                            .serviceImpl("/serviceImpl.java.ftl")
                            .mapper("/mapper.java.ftl")
                            .mapperXml("/mapper.xml.ftl")
                            .controller("/controller.java.ftl")
                            .build();
                })
                // 策略配置
                .strategyConfig((builder) -> builder.addInclude(TABLE_NAME).addTablePrefix(PREFIX)
                        .controllerBuilder().superClass(BaseController.class).enableRestStyle()
                        .entityBuilder()
                        .naming(NamingStrategy.underline_to_camel)
                        .columnNaming(NamingStrategy.underline_to_camel)
                        .enableLombok()
                        .enableRemoveIsPrefix()
                        .mapperBuilder()
                        .enableBaseColumnList()
                        .superClass(BaseMapper.class)
                        .build())
                .templateEngine(new EnhanceFreemarkerTemplateEngine())
                .execute();
    }

}
