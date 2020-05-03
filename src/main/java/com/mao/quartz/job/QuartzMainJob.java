package com.mao.quartz.job;

import com.mao.quartz.bean.QuartzTaskInfo;
import com.mao.quartz.util.HttpClientUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 通过传入url调用相关接口
 * @author bigdope
 * @create 2020-01-07
 **/
public class QuartzMainJob implements Job {

    private QuartzTaskInfo quartzTaskInfo;

    public QuartzTaskInfo getQuartzTaskInfo() {
        return quartzTaskInfo;
    }

    public void setQuartzTaskInfo(QuartzTaskInfo quartzTaskInfo) {
        this.quartzTaskInfo = quartzTaskInfo;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(quartzTaskInfo.getTaskName() + "任务开始执行");
        System.out.println("开始: " + System.currentTimeMillis());

        String result = HttpClientUtil.doGet(quartzTaskInfo.getUrl());
        System.out.println("请求结果: " + result);

        System.out.println("结束: " + System.currentTimeMillis());
        System.out.println(quartzTaskInfo.getTaskName() + "任务结束执行");
    }

}
