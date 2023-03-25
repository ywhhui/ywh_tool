package com.szcgc.cougua.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * @XxlJob 方式
 *  优点：有界面管理定时任务，支持弹性扩容缩容、动态分片、故障转移、失败报警等功能。它的功能非常强大，很多大厂在用，可以满足绝大多数业务场景。
    缺点：和 quartz 一样，通过数据库分布式锁，来控制任务不能重复执行。在任务非常多的情况下，有一些性能问题。
 */
@Component
public class TestXxlJobHandler{

    @XxlJob("xxlJobTest")
    public ReturnT<String> xxlJobTest(String date) {
        System.out.println("---------xxlJobTest定时任务执行成功6666--------");
        return ReturnT.SUCCESS;
    }
}
