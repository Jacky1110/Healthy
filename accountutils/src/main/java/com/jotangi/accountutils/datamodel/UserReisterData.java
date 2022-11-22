package com.jotangi.accountutils.datamodel;

import com.google.gson.annotations.SerializedName;

public class UserReisterData {
    public final static String KEY_NAME = AccountConstants.KEY_NAME;
    @SerializedName(KEY_NAME)
    private String name;

    public final static String KEY_EMAIL = AccountConstants.KEY_EMAIL;
    @SerializedName(KEY_EMAIL)
    private String email;

    public final static String KEY_MOBILE = AccountConstants.KEY_MOBILE;
    @SerializedName(KEY_MOBILE)
    private String mobile;

    public final static String KEY_PASSWORD = AccountConstants.KEY_PASSWORD;
    @SerializedName(KEY_PASSWORD)
    private String password;

    public final static String KEY_INVITE_CODE = "invitecode";
    @SerializedName(KEY_INVITE_CODE)
    private String invitecode;

    public UserReisterData() {
        this("","","","");
    }

    public UserReisterData(String name, String email, String mobile, String password) {
        this(name, email, mobile, password, "");
    }

    public UserReisterData(String name, String email, String mobile, String password, String invitecode) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.invitecode = invitecode;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public String getInvitecode() {
        return invitecode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setInvitecode(String invitecode) {
        this.invitecode = invitecode;
    }
}
