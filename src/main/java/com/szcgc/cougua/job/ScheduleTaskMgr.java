package com.szcgc.cougua.job;

/**
 * 具体的spring task 的执行类 写成单例 防止分布式环境 重复执行
 * 方法：多机器 加同步锁只一个执行
 */
public class ScheduleTaskMgr {
    //初始化单例对象
    private static ScheduleTaskMgr instance;

    /**
     * 实例化单例 task 具体任务同一个时间点 只有一个服务器去执行 避免重复执行
     * @return
     */
    public static ScheduleTaskMgr getInstance(){
        if(instance == null){
            synchronized (ScheduleTaskMgr.class){
                instance = new ScheduleTaskMgr();
            }
        }
        return instance;
    }

    public void executeTask(){
        //业务逻辑
        System.out.println("spring Task synchronized同步锁 分布式环境执行");
    }
}
