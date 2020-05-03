package com.mao.quartz.job;

import com.mao.quartz.ApplicationStartQuartzJobListener;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author bigdope
 * @create 2020-01-07
 **/
public class SchedulerQuartzJob1 implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("任务开始执行1");
        System.out.println("开始: " + System.currentTimeMillis());
        // TODO
        // 业务
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        ApplicationStartQuartzJobListener applicationStartQuartzJobListener = (ApplicationStartQuartzJobListener) jobDataMap.get("applicationStartQuartzJobListener");
        System.out.println(applicationStartQuartzJobListener.getInfo());


        System.out.println("结束: " + System.currentTimeMillis());
        System.out.println("任务结束执行1");
    }

}
