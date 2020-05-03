package com.mao.quartz.controller;

import com.mao.quartz.QuartzScheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * https://blog.csdn.net/upxiaofeng/article/details/79415108 -- SpringBoot集成Quartz动态定时任务
 * @author bigdope
 * @create 2020-01-07
 **/
@RestController
@RequestMapping("/quartz")
public class QuartzApiController {

    @Autowired
    @Qualifier("ownerQuartzScheduler")
    private QuartzScheduler quartzScheduler;

    @RequestMapping(value = "/startJob")
    public void startJob() throws SchedulerException {
        quartzScheduler.startJob();
    }

    @RequestMapping(value = "/getJobInfo")
    public String getJobInfo(String name, String group) throws SchedulerException {
        return quartzScheduler.getJobInfo(name, group);
    }

    @RequestMapping(value = "/modifyJob")
    public boolean modifyJob(String name, String group, String time) throws SchedulerException {
        return quartzScheduler.modifyJob(name, group, time);
    }

    @RequestMapping(value = "/pauseAllJob")
    public void pauseAllJob() throws SchedulerException {
        quartzScheduler.pauseAllJob();
    }

    @RequestMapping(value = "/pauseJob")
    public void pauseJob(String name, String group) throws SchedulerException {
        quartzScheduler.pauseJob(name, group);
    }

    @RequestMapping(value = "/resumeAllJob")
    public void resumeAllJob() throws SchedulerException {
        quartzScheduler.resumeAllJob();
    }

    @RequestMapping(value = "/resumeJob")
    public void resumeJob(String name, String group) throws SchedulerException {
        quartzScheduler.resumeJob(name, group);
    }

    @RequestMapping(value = "/deleteJob")
    public void deleteJob(String name, String group) throws SchedulerException {
        quartzScheduler.deleteJob(name, group);
    }

    @RequestMapping(value = "/addJob")
    public void addJob(String name, String group) throws SchedulerException {
        quartzScheduler.addJob(name, group);
    }

    @RequestMapping(value = "/addJobUrl")
    public void addJob(String name, String group, String url) throws SchedulerException {
        quartzScheduler.addJob(name, group, url);
    }

    @RequestMapping(value = "/testData")
    public String addJob(String data) throws SchedulerException {
        return "测试数据 data: " + data;
    }
}
