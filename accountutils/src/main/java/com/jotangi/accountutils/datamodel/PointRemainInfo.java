package com.jotangi.accountutils.datamodel;

public class PointRemainInfo {
    private int remainPoint;
    private int pointBeforeExpTime;
    private String expTime;

    public PointRemainInfo() {
    }

    public PointRemainInfo(int remainPoint, int pointBeforeExpTime, String expTime) {
        this.remainPoint = remainPoint;
        this.pointBeforeExpTime = pointBeforeExpTime;
        this.expTime = expTime;
    }

    public int getRemainPoint() {
        return remainPoint;
    }

    public String getExpTime() {
        return expTime;
    }

    public int getPointBeforeExpTime() {
        return pointBeforeExpTime;
    }

    public void setRemainPoint(int remainPoint) {
        this.remainPoint = remainPoint;
    }

    public void setExpTime(String expTime) {
        this.expTime = expTime;
    }

    public void setPointBeforeExpTime(int pointBeforeExpTime) {
        this.pointBeforeExpTime = pointBeforeExpTime;
    }
}
