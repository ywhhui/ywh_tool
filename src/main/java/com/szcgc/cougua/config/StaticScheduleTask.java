package com.szcgc.cougua.config;

import com.szcgc.cougua.job.ScheduleTaskMgr;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * spring task 分布式环境 某一个任务执行 避免重复
 */
@Configuration
@EnableScheduling
public class StaticScheduleTask {

    @Scheduled(cron = "0 0/1 * * * ? ")
    public static void refreshScheduleTask(){
        //先初始化某个需要执行的任务对象
        ScheduleTaskMgr.getInstance().executeTask();
    }
}
