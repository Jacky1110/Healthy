package com.jotangi.accountutils.datamodel;

public class PointTradeRecord {
    public final static int POINT_GET = 1;
    public final static int POINT_CANCEL = 2;
    public final static int POINT_USED = 3;

    private int pointType;
    private String recordTime;
    private int pointCount;
    private String expTime;

    public PointTradeRecord() {
    }

    public PointTradeRecord(int pointType, String recordTime, int pointCount, String expTime) {
        this.pointType = pointType;
        this.recordTime = recordTime;
        this.pointCount = pointCount;
        this.expTime = expTime;
    }

    public int getPointType() {
        return pointType;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public int getPointCount() {
        return pointCount;
    }

    public String getExpTime() {
        return expTime;
    }

    public void setPointType(int pointType) {
        this.pointType = pointType;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public void setPointCount(int pointCount) {
        this.pointCount = pointCount;
    }

    public void setExpTime(String expTime) {
        this.expTime = expTime;
    }
}
