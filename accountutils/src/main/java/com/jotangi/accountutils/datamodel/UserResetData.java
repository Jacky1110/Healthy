package com.jotangi.accountutils.datamodel;

public class UserResetData {
    private String mobile;
    private String password;
    private String verifycode;

    public UserResetData() {
    }

    public UserResetData(String mobile, String password, String verifycode) {
        this.mobile = mobile;
        this.password = password;
        this.verifycode = verifycode;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public String getVerifycode() {
        return verifycode;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setVerifycode(String verifycode) {
        this.verifycode = verifycode;
    }
}
