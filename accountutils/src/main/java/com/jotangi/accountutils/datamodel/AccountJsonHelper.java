package com.jotangi.accountutils.datamodel;

import android.content.Context;

import com.jotangi.baseutils.JsonHelper;
import com.jotangi.baseutils.Logger;

import org.json.JSONObject;

public class AccountJsonHelper extends JsonHelper {
    public AccountJsonHelper(Context context, Logger logger) {
        super(context, logger);
    }

    //==============================================================================================
    // UserData
    //==============================================================================================
    public UserData userDataFromJSON(JSONObject jo) {
        String[] checkkeys = {
                UserData.KEY_MOBILE,
                UserData.KEY_NAME,
        };
        try {
            if (hasKeys(jo, checkkeys)) {
                String mobile = jo.getString(UserData.KEY_MOBILE);
                String name = jo.getString(UserData.KEY_NAME);
                UserData userData = new UserData(name, mobile);
                if (hasKey(jo, UserData.KEY_ID_CODE)) userData.setIdcode(jo.getString(UserData.KEY_ID_CODE));
                if (hasKey(jo, UserData.KEY_SEX)) userData.setSex(jo.getInt(UserData.KEY_SEX));
                if (hasKey(jo, UserData.KEY_EMAIL)) userData.setEmail(jo.getString(UserData.KEY_EMAIL));
                if (hasKey(jo, UserData.KEY_PHONE)) userData.setPhone(jo.getString(UserData.KEY_PHONE));
                if (hasKey(jo, UserData.KEY_BIRTHDAY)) userData.setBirthday(jo.getString(UserData.KEY_BIRTHDAY));
                if (hasKey(jo, UserData.KEY_ADDRESS)) userData.setAddress(jo.getString(UserData.KEY_ADDRESS));
                if (hasKey(jo, UserData.KEY_PASSWORD)) userData.setPassword(jo.getString(UserData.KEY_PASSWORD));
                if (hasKey(jo, UserData.KEY_HEIGHT)) userData.setHeight(jo.getInt(UserData.KEY_HEIGHT));
                if (hasKey(jo, UserData.KEY_WEIGHT)) userData.setWeight(jo.getInt(UserData.KEY_WEIGHT));
                return userData;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public UserData userDataFromJSON(String data) {
        try {
            JSONObject jo = new JSONObject(data);
            return userDataFromJSON(jo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //==============================================================================================
    // UserLoginData
    //==============================================================================================
    public UserLoginData userLoginDataFromJSON(JSONObject jo) {
        String[] checkkeys = {
                UserLoginData.KEY_MOBILE,
                UserLoginData.KEY_PASSWORD
        };
        try {
            if (hasKeys(jo, checkkeys)) {
                String mobile = jo.getString(UserData.KEY_MOBILE);
                String pwd = jo.getString(UserData.KEY_PASSWORD);
                return new UserLoginData(mobile, pwd);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public UserLoginData userLoginDataFromJSON(String data) {
        try {
            JSONObject jo = new JSONObject(data);
            return userLoginDataFromJSON(jo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //==============================================================================================
    // UserReisterData
    //==============================================================================================
    public UserReisterData userRegisterDataFromJSON(JSONObject jo) {
        String[] checkkeys = {
                UserReisterData.KEY_NAME,
                UserReisterData.KEY_MOBILE,
                UserReisterData.KEY_EMAIL,
                UserReisterData.KEY_PASSWORD,
        };
        try {
            if (hasKeys(jo, checkkeys)) {
                String name = jo.getString(UserReisterData.KEY_NAME);
                String mobile = jo.getString(UserReisterData.KEY_MOBILE);
                String email = jo.getString(UserReisterData.KEY_EMAIL);
                String pwd = jo.getString(UserReisterData.KEY_PASSWORD);
                if (hasKey(jo, UserReisterData.KEY_INVITE_CODE)) {
                    String invitecode = jo.getString(UserReisterData.KEY_INVITE_CODE);
                    return new UserReisterData(name, email, mobile, pwd, invitecode);
                } else {
                    return new UserReisterData(name, email, mobile, pwd);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public UserReisterData userRegisterDataFromJSON(String data) {
        try {
            JSONObject jo = new JSONObject(data);
            return userRegisterDataFromJSON(jo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
