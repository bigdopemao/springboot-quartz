package com.mao.quartz.bean;

/**
 * @author bigdope
 * @create 2020-01-07
 **/
public class QuartzTaskInfo {

    private String taskName;

    private String url;

    public QuartzTaskInfo() {
    }

    public QuartzTaskInfo(String taskName, String url) {
        this.taskName = taskName;
        this.url = url;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
