package com.szcgc.cougua;

import com.szcgc.cougua.utils.ScheduledTasksUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        //jdk定时任务
        ScheduledTasksUtils.JdkThreadSleep();
        ScheduledTasksUtils.timerTest();
        ScheduledTasksUtils.scheduledExecutorServiceTest();
        //spring task
        SpringApplication.run(Application.class, args);
        System.out.println("----- http://localhost:8880/demo/ -----");
    }

}
