package com.mao.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author bigdope
 * @create 2020-01-07
 **/
public class SchedulerQuartzJob2 implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("任务开始执行2");
        System.out.println("开始: " + System.currentTimeMillis());
        // TODO
        // 业务

        System.out.println("结束: " + System.currentTimeMillis());
        System.out.println("任务结束执行2");
    }

}
