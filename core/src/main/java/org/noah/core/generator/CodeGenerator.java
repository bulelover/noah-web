package org.noah.core.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.noah.core.common.BaseController;
import org.noah.core.common.BaseMapper;

import java.util.HashMap;
import java.util.Map;

public class CodeGenerator {
    private static final String AUTHOR = "Noah";
    private static final String ROOT_PATH = "/module/file";
    private static final String MODULE_PARENT = "org.noah";
//    private static final String ROOT_PATH = "/admin";
//    private static final String MODULE_PARENT = "org.noah.web";
    private static final String MODULE = "file";
    private static final String PREFIX = "";
    private static final String[] TABLE_NAME = {"SYS_FILE"}; //SYS_DICT_ITEM
    private static final String pojoPackage = "file"; //dict

    public static void main(String[] args) {
        DataSourceConfig.Builder dscBuilder = new DataSourceConfig
                .Builder("jdbc:mysql://localhost:3306/frame?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false",
                "root", "Ro0t!@#");
        Map<OutputFile, String> pathInfo = new HashMap<>();
        //程序当前路径
        String projectPath = System.getProperty("user.dir") + ROOT_PATH;
        String modulePath = projectPath +"/src/main/java/"+MODULE_PARENT.replace(".","/")+"/" + MODULE;
        pathInfo.put(OutputFile.entity, modulePath + "/pojo/"+pojoPackage);

        FastAutoGenerator.create(dscBuilder)
                // 全局配置
                .globalConfig((builder) -> builder.author(AUTHOR) //.fileOverride()
                        .outputDir(projectPath +"/src/main/java")
                        .enableSwagger()
                        .disableOpenDir())
                // 包配置
                .packageConfig((builder) -> builder.parent(MODULE_PARENT).moduleName(MODULE)
                        .entity("pojo."+pojoPackage)
                        .pathInfo(pathInfo))
//                .injectionConfig((builder) -> builder.beforeOutputFile((tableInfo, objectMap) -> {
//                            System.out.println("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size());
//                        })
//                        .customMap(Collections.singletonMap("test", "baomidou"))
//                        .customFile(Collections.singletonMap("test.txt", "/templates/test.ftl"))
//                )
//                .templateConfig(builder -> {
//
//                })
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
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
