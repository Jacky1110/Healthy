package com.jotangi.jotangi2021.AndroidModule.Api;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/*
 * 😀😀😀😀😀😀😀😀😀😀😀😀Kelly Note!!!!😀😀😀😀😀😀😀😀😀😀😀😀
 * =============================================================
 * String url = api_url + api_constant;-->using your url
 * if api call back 0x0200,it success
 * 有些沒有0x0200
 * 如果不是0X0200 feedback的字串 =code+response
 *
 *
 * */
public class ApiConUtils {
    private static final String TAG = ApiConUtils.class.getSimpleName();

    private static Handler handler;
    private static OkHttpClient client;


    public interface OnConnect {
        void onSuccess(final String jsonString) throws JSONException;

        void onFailure(final String message);
    }

    /*
     * Get Member_Login code ,using code to boolean
     *
     * */
    @NonNull

    //get_app_version
    public static void getAppVersion(
            String api_url,
            String api_constant,
            final OnConnect con
    ) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder().build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    con.onSuccess(body);

//                    if (code.equals("0x0200")) {
//                        con.onSuccess(code);
//                    } else {
//                        con.onSuccess(error);
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    //member_login
    public static void member_login(String api_url, String api_constant, String account, String pwd, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder().add("member_id", account).add("member_pwd", pwd).build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Log.d("登入", "fuck " + api_url + " " + api_constant + " " + account + " " + pwd);
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    con.onSuccess(body);

//                    if (code.equals("0x0200")) {
//                        con.onSuccess(code);
//                    } else {
//                        con.onSuccess(error);
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    /*
     * Member reset password
     * */
    public static void member_resetCode(String api_url, String api_constant, String account, String pwd, String code, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder().add("member_id", account).add("member_pwd", pwd).add("code", code).build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());
                    //{"status":"false","code":"0x0201","responseMessage":"ID or code is wrong!"}
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    String responseMessage = jsonObject.getString("responseMessage");
                    String error = "code :" + code + "responseMessage :" + responseMessage;

                    if (code.equals("0x0200")) {
                        con.onSuccess(code);
                    } else {
                        con.onSuccess(error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    /*
     * 新的會員註冊
     * */
    public static void MemberRegi(String api_url, String api_constant, String account, String pwd, String name, String type, String email, String recommend, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody.Builder RegiBody = new FormBody.Builder();
        Log.d("登入", "fuckRecommend " + recommend + "\r\n"
                + "\r\n" + api_url + "\r\n"
                + "\r\n" + api_constant + "\r\n" + "\r\n" + account + "\r\n"
                + "\r\n" + recommend + "\r\n");
        RegiBody.add("member_id", account)
                .add("member_pwd", pwd)
                .add("member_name", name)
                .add("member_type", type)
                .add("recommend_code", recommend)
                .add("member_email", email);

        Request request = new Request.Builder().url(url).post(RegiBody.build()).build();
        if (client == null) client = new OkHttpClient();
        handler = new Handler();
        client.newCall(request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                con.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = null;
                ResponseBody responseBody = response.body();
                if (responseBody != null)
                    body = responseBody.string();
                Log.d("登入", "Recommend " + body);
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    String responseMessage = jsonObject.getString("responseMessage");
                    String error = "code :" + code + "responseMessage :" + responseMessage;
                    if (code.equals("0x0200")) {
                        con.onSuccess(code);
                    } else {
                        con.onSuccess(code);
                    }
                } catch (JSONException e) {
                    con.onFailure(e.getMessage());
                }
            }
        });

    }


    /*
     * Get user Code
     * */
    public static void member_usercode(String api_url, String api_constant, String account, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account).build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
//                    if (responseBody != null)
//                        body = responseBody.string();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Log.e(TAG, jsonObject.toString());
                    //{"status":"false","code":"0x0201","responseMessage":"ID or code is wrong!"}
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    String responseMessage = jsonObject.getString("responseMessage");
                    String error = "code :" + code + " responseMessage :" + responseMessage;

                    if (code.equals("0x0200")) {
                        con.onSuccess(code);
                    } else {
                        con.onSuccess(error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    /*
     * Get member information
     * */
    public static void member_info(String api_url, String api_constant, String account, String pwd, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);


                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    /*
     * Reset Member pwd
     * */
    public static void member_resetpwd(String api_url, String api_constant, String account, String pwd, String code, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("code", code).build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());
                    //{"status":"false","code":"0x0201","responseMessage":"ID or code is wrong!"}
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    String responseMessage = jsonObject.getString("responseMessage");
                    String error = "code :" + code + " responseMessage :" + responseMessage;

                    if (code.equals("0x0200")) {
                        con.onSuccess(code);
                    } else {
                        con.onSuccess(error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    /*
     * Reset Member information
     * */
    public static void member_edit(String api_url, String api_constant, String account, String pwd, String name, String gender, String email, String birthday, String phone, final OnConnect con) {
        String url = api_url + api_constant;

        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("member_name", name)
                .add("member_gender", gender)
                .add("member_email", email)
                .add("member_birthday", birthday)
                .add("member_phone", phone)
                .add("member_address", "test")
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                Log.d("三小  ", "" + body);

                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());
                    //{"status":"false","code":"0x0201","responseMessage":"ID or code is wrong!"}
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    String responseMessage = jsonObject.getString("responseMessage");
                    String error = "code :" + code + " responseMessage :" + responseMessage;

                    if (code.equals("0x0200")) {
                        con.onSuccess(code);
                    } else {
                        con.onSuccess(error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    //member_delete
    public static void member_delete(
            String api_url,
            String api_constant,
            String account,
            String pwd,
            final OnConnect con
    ) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add(
                        "member_id",
                        account
                )
                .add(
                        "member_pwd",
                        pwd
                ).build();

        Request request = new Request.Builder().url(url).post(formBody).build();
        Log.d("刪除", "fuck " + api_url + " " + api_constant + " " + account + " " + pwd);
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    con.onSuccess(body);

//                    if (code.equals("0x0200")) {
//                        con.onSuccess(code);
//                    } else {
//                        con.onSuccess(error);
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    /*
     * Get order list
     * */
    public static void order_list(String api_url, String api_constant, String account, String pwd, String startdate, String enddate, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
//                .add("order_startdate", startdate)
//                .add("order_enddate", enddate)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    con.onSuccess(body);

                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    /*
     * Get bonus
     * */
    public static void bonus_list(String api_url, String api_constant, String account, String pwd, String startdate, String enddate, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
//                .add("bonus_startdate", startdate)
//                .add("bonus_enddate", startdate)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    con.onSuccess(body);
                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    /*
     * Member login out
     *
     * */
    @NonNull
    public static void member_logout(String api_url, String api_constant, String account, String pwd, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder().add("member_id", account)
                .add("member_pwd", pwd).build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        handler = new Handler();
        client.newCall(request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                con.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = null;
                ResponseBody responseBody = response.body();
                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    String responseMessage = jsonObject.getString("responseMessage");
                    String error = "code :" + code + "responseMessage :" + responseMessage;
                    if (code.equals("0x0200")) {
                        con.onSuccess(code);
                    } else {
                        con.onSuccess(error);
                    }
                } catch (JSONException e) {
                    con.onFailure(e.getMessage());
                }
            }
        });

    }

    /*
     * Get payment url
     *
     * */
    @NonNull
    public static void payment_url(String api_url, String api_constant, String account, String pwd, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder().add("member_id", account)
                .add("member_pwd", pwd).build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        handler = new Handler();
        client.newCall(request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                con.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = null;
                ResponseBody responseBody = response.body();
                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    String responseMessage = jsonObject.getString("responseMessage");
                    String error = "code :" + code + "responseMessage :" + responseMessage;
                    if (code.equals("0x0200")) {
                        con.onSuccess(responseMessage);
                    } else {
                        con.onSuccess(body);
                    }
                } catch (JSONException e) {
                    con.onFailure(e.getMessage());
                }
            }
        });

    }

    /*
     * get coupon list
     *FIXME: 流程需要再跟EVA CHECK again，sid 目前認知是從barcode取得，
     * */
    public static void coupon_list(String api_url, String api_constant, String account, String pwd, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = null;
        formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)

                .build();

        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    Log.d(TAG, "Kelly find coupon list : " + body);

                    con.onSuccess(body);
                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    /*
     * get recommand list
     * "recommend_list": [
    {
      "0": "0965026589",
      "1": "2021-12-03",
      "2": "12342234",
      "recommend_code": "0965026589",
      "recommend_date": "2021-12-03",
      "recommend_member": "12342234"
    }
  ]
     *
     * */
    public static void recommand_list(String api_url, String api_constant, String account, String pwd, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    Log.d(TAG, "Kelly find recommand list : " + body);

                    con.onSuccess(body);
                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    /*
     * Get product_type
     * */
    public static void product_type(
            String api_url,
            String api_constant,
//            String account,
//            String pwd,
            final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
//                .add("member_id", account)
//                .add("member_pwd", pwd)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * Get product_list
     * */
    public static void product_list(
            String api_url,
            String api_constant,
//            String account,
//            String pwd,
            final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
//                .add("member_id", account)
//                .add("member_pwd", pwd)
//                .add("product_no", pwd)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * Get product_info
     * */
    public static void product_info(String api_url, String api_constant,
//                                    String account,
//                                    String pwd,
                                    String type, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
//                .add("member_id", account)
//                .add("member_pwd", pwd)
                .add("product_no", type)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * add_shoppingcart新增商品到購物車
     * */
    public static void add_shoppingcart(String api_url, String api_constant, String account, String pwd, String no, Integer price, Integer qty, Integer amount, final OnConnect con) {
        String url = api_url + api_constant;
        Log.d("add_shoppingcart", "是甚麼壓~ : " + " account " + account + "\n"
                + "pwd : " + pwd + "\r\n" + "商品編號: " + no + "\r\n" + "價格: " + price + "\r\n" + "數量: " + qty + "\r\n" + "總計 :" + amount + "\r\n");
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("product_no", no)
                .add("product_price", String.valueOf(price))
                .add("order_qty", String.valueOf(qty))
                .add("total_amount", String.valueOf(amount))
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.d(TAG, "新增商品到購物車 " + body + "\n\n");

                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * shoppingcart_list購物車內商品列表
     * */
    public static void shoppingcart_list(String api_url, String api_constant, String account, String pwd, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * edit_shoppingcart 修改購物車內商品數量
     * */
    public static void edit_shoppingcart(String api_url, String api_constant, String account, String pwd, String no, String qty, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("product_no", no)
                .add("order_qty", qty)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * del_shoppingcart 修改購物車內商品數量
     * */
    public static void del_shoppingcart(String api_url, String api_constant, String account, String pwd, String no, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("product_no", no)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * shoppingcart_count 購物車內商品總數
     * */
    public static void shoppingcart_count(String api_url, String api_constant, String account, String pwd, final OnConnect con) {
        String url = api_url + api_constant;
        Log.e("fuck3", "shoppingcart_count " + account + pwd);
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     *add_ecorder 新增商城訂單
     * */
    public static void add_ecorder(String api_url
            , String api_constant, String account
            , String pwd, String order_amount, String couponNo
            , String discountAmount, String bonusPoint
            , String order_pay, String delivery_type
            , String recipient_name, String recipient_addr
            , String recipient_phone, String recipient_mail
            , String invoice_type, String invoice_phone
            , String company_title, String uniform_no
            , String sid, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody.Builder Builder = new FormBody.Builder();
//53758995
        Builder
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("order_amount", order_amount) // 應付
                .add("coupon_no", couponNo)
                .add("discount_amount", discountAmount)
                .add("bonus_point", bonusPoint)
                .add("order_pay", order_pay) // 實付
                .add("delivery_type", delivery_type)
                .add("recipient_name", recipient_name)
                .add("recipient_addr", recipient_addr)
                .add("recipient_phone", recipient_phone)
                .add("recipient_mail", recipient_mail)
                .add("invoice_type", invoice_type)
                .add("invoice_phone", invoice_phone)
                .add("company_title", company_title)
                .add("uniform_no", uniform_no)
                .add("sid", sid)
        ;
        Log.d(" 3 ", uniform_no + " " + company_title);
        Request request = new Request.Builder().url(url).post(Builder.build()).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(" add eco ", "甚麼勒~" + e.getMessage());

                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * ecorder_list 購物車內商品總數
     * */
    public static void ecorder_list(String api_url, String api_constant, String account, String pwd, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * ecorder_info 購物車內商品總數
     * */
    public static void ecorder_info(String api_url, String api_constant, String account, String pwd, String no, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("order_no", no)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*store_info*/
    public static void store_info(String api_url, String api_constant, String account, String pwd, String no, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("sid", no)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*clear shopping cart清空購物車*/
    public static void clear_shoppingcart(String api_url, String api_constant, String account, String pwd, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)

                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

}
