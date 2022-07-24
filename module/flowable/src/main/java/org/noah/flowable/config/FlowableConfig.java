package org.noah.flowable.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.AbstractEngineConfiguration;
import org.flowable.common.engine.impl.EngineConfigurator;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
@Slf4j
public class FlowableConfig implements EngineConfigurator {

    private static final AtomicBoolean initialized = new AtomicBoolean();


    @Override
    public void beforeInit(AbstractEngineConfiguration engineConfiguration) {
        if(initialized.compareAndSet(false, true)){
            DataSource dataSource = engineConfiguration.getDataSource();
            if(dataSource instanceof TransactionAwareDataSourceProxy){
                dataSource = ((TransactionAwareDataSourceProxy) dataSource).getTargetDataSource();
            }
            if(dataSource instanceof DynamicRoutingDataSource){
                DataSource ds = ((DynamicRoutingDataSource) dataSource).getDataSource("flowable");
                engineConfiguration.setDataSource(ds);
            }
            log.info("启动动态切换数据源。。。");
        }
    }

    @Override
    public void configure(AbstractEngineConfiguration engineConfiguration) {

    }

    @Override
    public int getPriority() {
        return 0;
    }
}
