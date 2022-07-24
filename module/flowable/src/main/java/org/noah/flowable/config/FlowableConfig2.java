package org.noah.flowable.config;

import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlowableConfig2 implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {

    @Override
    public void configure(SpringProcessEngineConfiguration con) {
        //防止生成的流程图中中文乱码
        con.setActivityFontName("宋体");
        con.setLabelFontName("宋体");
        con.setAnnotationFontName("宋体");
    }
}
