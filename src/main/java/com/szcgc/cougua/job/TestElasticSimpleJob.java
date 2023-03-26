package com.szcgc.cougua.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * SimpleJob 完成配置和初始化
 * 其他分布式定时任务
 * Saturn 是唯品会开源的一个分布式任务调度平台 取代传统的Linux Cron/Spring Batch Job的方式，做到全域统一配置，统一监控，任务高可用以及分片并发处理。
 * TBSchedule 是阿里开发的一款分布式任务调度平台，旨在将调度作业从业务系统中分离出来，降低或者是消除和业务系统的耦合度，进行高效异步任务处理。
 */
public class TestElasticSimpleJob implements SimpleJob{

    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println(String.format("------elastic job-Thread ID: %s, 任务总片数: %s, " +
                        "当前分片项: %s.当前参数: %s,"+
                        "当前任务名称: %s.当前任务参数: %s"
                ,
                Thread.currentThread().getId(),
                shardingContext.getShardingTotalCount(),
                shardingContext.getShardingItem(),
                shardingContext.getShardingParameter(),
                shardingContext.getJobName(),
                shardingContext.getJobParameter()

        ));
    }
}
