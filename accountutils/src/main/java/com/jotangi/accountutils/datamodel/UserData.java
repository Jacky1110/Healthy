package com.jotangi.accountutils.datamodel;

import com.google.gson.annotations.SerializedName;

public class UserData {
    public final static int USER_UNKNOW = 0;
    public final static int USER_MAN = 1;
    public final static int USER_WOMAN = 2;

    public final static String KEY_ID_CODE = AccountConstants.KEY_ID_CODE;
    @SerializedName(KEY_ID_CODE)
    private String idcode;

    public final static String KEY_NAME = AccountConstants.KEY_NAME;
    @SerializedName(KEY_NAME)
    private String name;

    public final static String KEY_SEX = AccountConstants.KEY_SEX;
    @SerializedName(KEY_SEX)
    private int sex;

    public final static String KEY_EMAIL = AccountConstants.KEY_EMAIL;
    @SerializedName(KEY_EMAIL)
    private String email;

    public final static String KEY_MOBILE = AccountConstants.KEY_MOBILE;
    @SerializedName(KEY_MOBILE)
    private String mobile;

    public final static String KEY_PHONE = AccountConstants.KEY_PHONE;
    @SerializedName(KEY_PHONE)
    private String phone;

    public final static String KEY_BIRTHDAY = AccountConstants.KEY_BIRTHDAY;
    @SerializedName(KEY_BIRTHDAY)
    private String birthday;

    public final static String KEY_ADDRESS = AccountConstants.KEY_ADDRESS;
    @SerializedName(KEY_ADDRESS)
    private String address;

    public final static String KEY_PASSWORD = AccountConstants.KEY_PASSWORD;
    @SerializedName(KEY_PASSWORD)
    private String password;

    public final static String KEY_HEIGHT = AccountConstants.KEY_HEIGHT;
    @SerializedName(KEY_HEIGHT)
    private int height;

    public final static String KEY_WEIGHT = AccountConstants.KEY_WEIGHT;
    @SerializedName(KEY_WEIGHT)
    private int weight;

    public UserData() {
        this(
                "",
                "",
                USER_UNKNOW,
                "",
                "",
                "",
                "",
                "",
                "",
                0,
                0
        );
    }

    public UserData(String name, String mobile) {
        this(
                "",
                name,
                USER_UNKNOW,
                "",
                mobile,
                "",
                "",
                "",
                "",
                0,
                0
        );
    }

    public UserData(String idcode, String name, int sex, String email, String mobile, String phone, String birthday, String address, String password, int height, int weight) {
        this.idcode = idcode;
        this.name = name;
        this.sex = sex;
        this.email = email;
        this.mobile = mobile;
        this.phone = phone;
        this.birthday = birthday;
        this.address = address;
        this.password = password;
        this.height = height;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getSex() {
        return sex;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPhone() {
        return phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getIdcode() {
        return idcode;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setIdcode(String idcode) {
        this.idcode = idcode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
