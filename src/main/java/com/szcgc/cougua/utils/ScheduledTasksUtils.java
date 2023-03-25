package com.szcgc.cougua.utils;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务总结
 *
 */
@Component
public class ScheduledTasksUtils {

    /**
     * 一.jdk自带的 Thread
     * 优点 一次性的 异常了while就break了
     * 缺点 周期性 不能cron配置某一个时间点执行
     */
    public static void JdkThreadSleep(){
        //当守护线程 监控服务执行 如果异常就终止
        new Thread(()->{
            //死循环
            while (true){
                try {
                    //业务逻辑
                    System.out.println("doSameThing1 JdkThreadSleep");
                    Thread.sleep(1000 * 60 * 1);
                } catch (Exception e) {
                    break;
                }
            }
        }).start();
    }

    /**
     * 二.jdk java.util 包下Timer
     * 优点 支持延迟执行，还支持在指定时间之后支持
     * 缺点;其中一个任务耗时非常长，会影响其他任务的执行。并且如果 TimerTask 抛出 RuntimeException ， Timer 会停止所有任务的运行，所以阿里巴巴开发者规范中不建议使用它
     */
    public static void timerTest(){
        Timer timer = new Timer();
        //指定任务task 在指定延迟delay 后，进行重复固定延迟频率peroid的执行
        try {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("doSomething2 timerTest");
                }
            },1000,60000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 三.ScheduledExecutorService JDK1.5+版本引进的定时任务java.util.concurrent并发包下
     *优点： 多个任务之间不会相关影响，支持周期性的执行任务，并且带延迟功能  不支持一些较复杂的定时规则。
     * 缺点：不支持一些较复杂的定时规则。
     */
    public static void scheduledExecutorServiceTest(){
        try {
            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                System.out.println("doSomething3 scheduledExecutorServiceTest");
            },1000,60000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *四。spring-context 包
     * 优点：springboot做了非常好的封装，开启和定义定时任务非常容易，支持复杂的 cron 表达式，可以满足绝大多数单机版的业务场景。单个任务时，当前次的调度完成后，再执行              下一次任务调度
     * 缺点：默认单线程，如果前面的任务执行时间太长，对后面任务的执行有影响。不支持集群方式部署，不能做数据存储型定时任务。
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public static void springTaskTest(){
        System.out.println("doSomething4 springTaskTest");
    }

    /**
     * 五。quartz
     * 优点：默认是多线程异步执行，单个任务时，在上一个调度未完成时，下一个调度时间到时，会另起一个线程开始新的调度，多个任务之间互不影响。支持复杂的 cron 表达式，它能被集群实例化，支持分布式部署。
     缺点：相对于spring task实现定时任务成本更高，需要手动配置 QuartzJobBean 、 JobDetail和 Trigger 等。需要引入了第三方的 quartz 包，有一定的学习成本。不支持并行调度，不支持失败处理策略和动态分片的策略等。

     */
    public static class QuartzTestJob extends QuartzJobBean{
        @Override
        protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
            String  userName = context.getJobDetail().getJobDataMap().get("userName").toString();
            System.out.println("doSomething5 quartzTest----"+userName);
        }
    }


}
