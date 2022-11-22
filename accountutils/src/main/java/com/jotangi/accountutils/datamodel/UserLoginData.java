package com.jotangi.accountutils.datamodel;

import com.google.gson.annotations.SerializedName;

public class UserLoginData {
    public final static String KEY_MOBILE = AccountConstants.KEY_MOBILE;
    @SerializedName(KEY_MOBILE)
    private String mobile;

    public final static String KEY_PASSWORD = AccountConstants.KEY_PASSWORD;
    @SerializedName(KEY_PASSWORD)
    private String password;

    public UserLoginData() {
    }

    public UserLoginData(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
