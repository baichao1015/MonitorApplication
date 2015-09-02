package com.bangbang.baichao.monitorapplication.entity;

/**
 * 监控信息实体
 * Created by 58 on 2015/7/23.
 */
public class MonitorVO {

    private int id;
    private String serve;
    private String path;
    private String interval;
    private String threshold;
    private String name;

    public MonitorVO(int id, String serve, String path, String interval, String threshold, String name) {
        this.id = id;
        this.serve = serve;
        this.interval = interval;
        this.threshold = threshold;
        this.path = path;
        this.name = name;

    }

    public MonitorVO(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServe() {
        return serve;
    }

    public void setServe(String serve) {
        this.serve = serve;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
