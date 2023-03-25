package com.szcgc.cougua.job;

import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Component;

/**
 *JobHandler 被XxlJob替代
 */
@JobHandler("JobHandlerZj")
@Component
public class TestJobHandler extends IJobHandler{
    @Override
    public void execute() throws Exception {
        System.out.println("66JobHandlerZj");
    }
}
