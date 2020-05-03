package com.mao.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author bigdope
 * @create 2020-01-07
 **/
@Configuration
public class ApplicationStartQuartzJobListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    @Qualifier("ownerQuartzScheduler")
    private QuartzScheduler quartzScheduler;

    /**
     * 初始启动quartz
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            quartzScheduler.startJob();
            System.out.println("任务已经启动");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始注入scheduler
     * @return
     */
    @Bean
    public Scheduler scheduler() throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        return schedulerFactory.getScheduler();
    }

    public String getInfo() {
        return "applicationStartQuartzJobListener 测试输出数据";
    }
}
