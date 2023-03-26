package com.szcgc.cougua.job;

import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Component;

/**
 *JobHandler 被XxlJob替代 有问题的写法 未实现
 */
@JobHandler("jobHandlerZj")
@Component
public class TestJobHandler extends IJobHandler{
    @Override
    public void execute() throws Exception {
        System.out.println("66JobHandlerZj");
    }
}
