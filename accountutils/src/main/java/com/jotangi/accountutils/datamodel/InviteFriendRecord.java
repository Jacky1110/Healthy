package com.jotangi.accountutils.datamodel;

public class InviteFriendRecord {

    private int awardType;
    private String awardTypeName;
    private int awardPoint;
    private String datetime;
    private String phone;

    public InviteFriendRecord() {
    }

    public InviteFriendRecord(int awardType, String awardTypeName, int awardPoint, String datetime, String phone) {
        this.awardType = awardType;
        this.awardTypeName = awardTypeName;
        this.awardPoint = awardPoint;
        this.datetime = datetime;
        this.phone = phone;
    }

    public int getAwardType() {
        return awardType;
    }

    public int getAwardPoint() {
        return awardPoint;
    }

    public String getAwardTypeName() {
        return awardTypeName;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getPhone() {
        return phone;
    }

    public void setAwardType(int awardType) {
        this.awardType = awardType;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setAwardPoint(int awardPoint) {
        this.awardPoint = awardPoint;
    }

    public void setAwardTypeName(String awardTypeName) {
        this.awardTypeName = awardTypeName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
