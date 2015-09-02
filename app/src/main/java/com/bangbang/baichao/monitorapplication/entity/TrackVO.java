package com.bangbang.baichao.monitorapplication.entity;

/**
 * Created by 58 on 2015/7/22.
 */
public class TrackVO {
    private String serve;
    private String time;
    private String monitorCount;
    private String failCount;

    public TrackVO(String serve, String time, String monitorCount, String failCount) {
        this.serve = serve;
        this.time = time;
        this.monitorCount = monitorCount;
        this.failCount = failCount;

    }

    public String getServe() {
        return serve;
    }

    public void setServe(String serve) {
        this.serve = serve;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMonitorCount() {
        return monitorCount;
    }

    public void setMonitorCount(String monitorCount) {
        this.monitorCount = monitorCount;
    }

    public String getFailCount() {
        return failCount;
    }

    public void setFailCount(String failCount) {
        this.failCount = failCount;
    }
}
