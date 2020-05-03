package com.mao.quartz;

import com.mao.quartz.bean.QuartzTaskInfo;
import com.mao.quartz.job.QuartzMainJob;
import com.mao.quartz.job.SchedulerQuartzJob1;
import com.mao.quartz.job.SchedulerQuartzJob2;
import com.mao.quartz.job.ZKSchedulerQuartzJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 任务调度处理
 * @author bigdope
 * @create 2020-01-07
 **/
@Service("ownerQuartzScheduler")
public class QuartzScheduler {

    // 任务调度
    @Autowired
    private Scheduler scheduler;

    /**
     * 开始执行所有任务
     * @throws SchedulerException
     */
    public void startJob() throws SchedulerException {
//        startJob1(scheduler);
//        if (!scheduler.isStarted()) {
//            scheduler.start();
//        }
        startZkJob(scheduler);
        scheduler.start();
    }

    /**
     * 获取Job信息
     * @param name 触发器名
     * @param group 触发器组名
     * @return
     * @throws SchedulerException
     */
    public String getJobInfo(String name, String group) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        return String.format("time:%s, state:%s", cronTrigger.getCronExpression(),
                scheduler.getTriggerState(triggerKey).name());
    }

    /**
     * 修改某个任务的执行时间
     * 一个任务可能有多个触发器
     * @param name 触发器名
     * @param group 触发器组名
     * @param time
     * @return
     */
    public boolean modifyJob(String name, String group, String time) throws SchedulerException {
        Date date = null;
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldTime = trigger.getCronExpression();
        if (!oldTime.equals(time)) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(name, group)
                    .withSchedule(cronScheduleBuilder)
                    .build();
            date = scheduler.rescheduleJob(triggerKey, cronTrigger);
        }
        return date != null;
    }

    /**
     * 暂停所有任务
     * @throws SchedulerException
     */
    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }

    /**
     *  暂停某个任务
     * @param name 任务名
     * @param group 任务组名
     * @throws SchedulerException
     */
    public void pauseJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return;
        }
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复所有任务
     * @throws SchedulerException
     */
    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    /**
     * 恢复某个任务
     * @param name 任务名
     * @param group 任务组名
     */
    public void resumeJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return;
        }
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除某个任务
     * @param name 任务名
     * @param group 任务组名
     */
    public void deleteJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return;
        }
        scheduler.deleteJob(jobKey);
    }

    /**
     * 新增任务
     * @param name 任务名
     * @param group 任务组名
     * @throws SchedulerException
     */
    public void addJob(String name, String group) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(SchedulerQuartzJob2.class)
                .withIdentity(name, group)
                .build();
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(name, group)
                .withSchedule(scheduleBuilder)
                .build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    /**
     * 新增任务
     * @param name 任务名
     * @param group 任务组名
     * @param url get请求地址
     * @throws SchedulerException
     */
    public void addJob(String name, String group, String url) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(QuartzMainJob.class)
                .withIdentity(name, group)
                .build();
        QuartzTaskInfo quartzTaskInfo = new QuartzTaskInfo(name, url);
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        jobDataMap.put("quartzTaskInfo", quartzTaskInfo);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(name, group)
                .withSchedule(scheduleBuilder)
                .build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    @Autowired
    private ApplicationStartQuartzJobListener applicationStartQuartzJobListener;

    private void startJob1(Scheduler scheduler) throws SchedulerException {
        // 测试传入对象数据 applicationStartQuartzJobListener
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("applicationStartQuartzJobListener", applicationStartQuartzJobListener);
        // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
        // JobDetail 是具体Job实例
        JobDetail jobDetail = JobBuilder.newJob(SchedulerQuartzJob1.class)
                .usingJobData(jobDataMap)
                .withIdentity("job1", "group1")
                .build();
        // 基于表达式构建触发器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/5 * * * * ?");
        // CronTrigger表达式触发器 继承于Trigger
        // TriggerBuilder 用于构建触发器实例
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("job1", "group1")
                .withSchedule(cronScheduleBuilder)
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    private void startZkJob(Scheduler scheduler) throws SchedulerException {
        // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
        // JobDetail 是具体Job实例
        JobDetail jobDetail = JobBuilder.newJob(ZKSchedulerQuartzJob.class)
                .withIdentity("job1", "group1")
                .build();
        // 基于表达式构建触发器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0/1 * * * ?");
        // CronTrigger表达式触发器 继承于Trigger
        // TriggerBuilder 用于构建触发器实例
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("job1", "group1")
                .withSchedule(cronScheduleBuilder)
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

}
