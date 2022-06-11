package org.noah.job.demo;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * 这里介绍
 * BEAN模式（方法形式）
 * 正常情况下，此种模式就够了
 * 要在调度中心，新建调度任务 对新建的任务进行参数配置，运行模式选中 “BEAN模式”，JobHandler属性填写任务注解“@XxlJob”中定义的值；
 */
@Component
public class DemoJob{

    /**
     *
     */
    @XxlJob("helloJobHandler")
    public void helloJobHandler() throws Exception {
        //执行日志
        System.out.println("XXL-JOB, Hello World.");
        //默认为成功通过，可通过一下方法设置失败和通过
//        XxlJobHelper.handleFail(msg);
//        XxlJobHelper.handleSuccess(msg);
    }
}
