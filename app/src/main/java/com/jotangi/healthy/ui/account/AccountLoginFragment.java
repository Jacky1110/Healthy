package com.jotangi.healthy.ui.account;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.jotangi.healthy.HpayMall.ApiUrl;
import com.jotangi.healthy.HpayMall.CustomDaialog;
import com.jotangi.healthy.HpayMall.Mall.SCartFragmemt;
import com.jotangi.healthy.HpayMall.MemberBean;
import com.jotangi.healthy.R;
import com.jotangi.healthy.ui.ProjConstraintFragment;
import com.jotangi.healthy.utils.ProjSharePreference;
import com.jotangi.jotangi2021.AndroidModule.Api.ApiConUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * ♡♡♡♡♡♡♡♡♡♡♡Kelly 's Note♡♡♡♡♡♡´ސު｀
 * ====================================================
 */
public class AccountLoginFragment extends ProjConstraintFragment {

    public final static String REG_PREF_NAME = "loginInfo";
    public final static String KEY_IS_LOGIN = "isLogin";
    public final static String KEY_ACCOUNT = "account";
    public final static String KEY_PASSWORD = "password";

    //private AccountViewModel accountViewModel;
    LayoutInflater rule;
    EditText edPhone, edPassword;
    TextView tvForgetPassword, tvRule;
    ConstraintLayout btnRegister, btnLogin;
    private ImageButton btnCart;

    private String account;

    public static AccountLoginFragment newInstance() {
        AccountLoginFragment fragment = new AccountLoginFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.login;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_account_login, container, false);

        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        edPhone = rootView.findViewById(R.id.ed_phone);
        edPassword = rootView.findViewById(R.id.ed_home_number);
        tvRule = rootView.findViewById(R.id.txv_rule);
        tvRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_LOGIN_TO_RULE, null);
                }
            }
        });

        btnRegister = rootView.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_LOGIN_TO_REGISTER, null);
                }
            }
        });

        btnLogin = rootView.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        tvForgetPassword = rootView.findViewById(R.id.tv_forget_password);
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_LOGIN_TO_FORGET_PASSWORD, null);
                }
            }
        });
        btnCart = getActivity().findViewById(R.id.btn_car);


        handleIcon();
        handleUserLogin();

        /*
        SharedPreferences pref = requireActivity().getSharedPreferences(REG_PREF_NAME, 0);

        boolean isLogin = pref.getBoolean(KEY_IS_LOGIN, true);
        if (isLogin) {
            edPhone.setText(pref.getString(KEY_ACCOUNT, ""));
            edPassword.setText(pref.getString(KEY_PASSWORD, ""));
            //loginProcess();
        }
        */
    }

    private void handleIcon() {
        if (MemberBean.member_id == null && MemberBean.member_pwd == null || MemberBean.member_id.equals("") && MemberBean.member_pwd.equals(""))
        {
            btnCart.setVisibility(View.INVISIBLE);
        } else{
            btnCart.setVisibility(View.VISIBLE);
        }
    }

    private void handleUserLogin() {
        SharedPreferences preferences = requireActivity().getSharedPreferences(REG_PREF_NAME, MODE_PRIVATE);
        boolean signed = preferences.getBoolean(KEY_IS_LOGIN, false);

        if (signed == true) {
            MemberBean.member_id = preferences.getString(KEY_ACCOUNT, "");
            MemberBean.member_pwd = preferences.getString(KEY_PASSWORD, "");

            //自動登入
            try {
                if (MemberBean.member_id != null && MemberBean.member_id.length() == 10) {

                    fragmentListener.onAction(FUNC_LOGIN_TO_ACCOUNT_MAIN, null);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void savaTermsStatus(boolean status, String code) {

        SharedPreferences pref = requireActivity().getSharedPreferences("UserTerms", MODE_PRIVATE);
        pref.edit()
                .putBoolean("Terms", status)
                .putString("code", code)
                .commit();
    }

    private void savaLoginStatus(boolean status, String account, String pwd) {

        SharedPreferences pref = requireActivity().getSharedPreferences(REG_PREF_NAME, MODE_PRIVATE);
        pref.edit()
                .putBoolean(KEY_IS_LOGIN, status)
                .putString(KEY_ACCOUNT, account)
                .putString(KEY_PASSWORD, pwd)
                .commit();
        Log.d("儲存", "成功" + status + "/" + account + "/" + pwd);
    }

    private void login() {
        String acc = edPhone.getText().toString();
        String pd = edPassword.getText().toString();
        ApiConUtils.member_login(
                ApiUrl.API_URL,
                ApiUrl.LOGIN,
                acc,
                pd,
                new ApiConUtils.OnConnect() {
                    @Override
                    public void onSuccess(String jsonString) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(jsonString);
                                    Log.e(TAG, jsonObject.toString());
                                    //{"status":"false","code":"0x0201","responseMessage":"ID or code is wrong!"}
                                    String status = jsonObject.getString("status");
                                    String code = jsonObject.getString("code");
                                    String responseMessage = jsonObject.getString("responseMessage");
                                    if (code.equals("0x0200")) {
                                        ProjSharePreference.setLoginState(getActivity(), true);
                                        MemberBean.member_id = edPhone.getText().toString().trim();
                                        MemberBean.member_pwd = edPassword.getText().toString().trim();
                                        savaLoginStatus(true, MemberBean.member_id, MemberBean.member_pwd);

                                        if (fragmentListener != null) {
//                                            fragmentListener.onAction(FUNC_LOGIN_TO_ACCOUNT_MAIN, null);
                                            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                            transaction.replace(R.id.nav_host_fragment_activity_main, AccountMainFragment.newInstance());
                                            transaction.commit();
                                        }

                                        if (MemberBean.member_id != null && MemberBean.member_pwd != null) {
                                            member_information();
                                        }
                                    } else {
                                        CustomDaialog.showNormal(requireActivity(), "", "登入失敗", "確定", new CustomDaialog.OnBtnClickListener() {
                                            @Override
                                            public void onCheck() {

                                            }

                                            @Override
                                            public void onCancel() {
                                                CustomDaialog.closeDialog();

                                            }
                                        });
                                    }

                                } catch (Exception e) {
                                }

                            }
                        });


                    }

                    @Override
                    public void onFailure(String message) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("登入", "成功" + message);
                                CustomDaialog.showNormal(requireActivity(), "", "登入失敗", "確定", new CustomDaialog.OnBtnClickListener() {
                                    @Override
                                    public void onCheck() {

                                    }

                                    @Override
                                    public void onCancel() {

//                                        fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_LOGIN, null);
                                        CustomDaialog.closeDialog();

                                    }
                                });
                                return;
//                        DialogUtils.showMyDialog(rootView, 0, R.string.login_fail, "", R.string.button_confirm, new DialogUtils.OneButtonClickListener() {
//                            @Override
//                            public void onButton1Clicked() {
//                                edPassword.setText("");
//                                edPhone.setText("");
//                            }
//                        });
//                        return;
                            }
                        });

                    }
                });
    }

    public static void member_information() {
        String account = MemberBean.member_id;
        String pwd = MemberBean.member_pwd;
        ApiConUtils.member_info(
                ApiUrl.API_URL,
                ApiUrl.member_info,
                account,
                pwd,
                new ApiConUtils.OnConnect() {
                    @Override
                    public void onSuccess(String jsonString) throws JSONException {
                        Log.d("會員資訊", " " + jsonString);
                        JSONArray jsonArray = new JSONArray(jsonString);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            MemberBean.mid = jsonObject.getString("mid");
                            MemberBean.member_id = jsonObject.getString("member_id");
                            MemberBean.member_pwd = jsonObject.getString("member_pwd");
                            MemberBean.member_name = jsonObject.getString("member_name");
                            MemberBean.member_type = jsonObject.getString("member_type");
                            MemberBean.member_gender = jsonObject.getString("member_gender");
                            MemberBean.member_email = jsonObject.getString("member_email");
                            MemberBean.member_birthday = jsonObject.getString("member_birthday");
                            MemberBean.member_address = jsonObject.getString("member_address");
                            MemberBean.member_phone = jsonObject.getString("member_phone");
                            MemberBean.member_picture = jsonObject.getString("member_picture");
                            MemberBean.member_totalpoints = jsonObject.getString("member_totalpoints");
                            MemberBean.member_usingpoints = jsonObject.getString("member_usingpoints");
                            MemberBean.member_status = jsonObject.getString("member_status");
                            MemberBean.recommand_code = jsonObject.getString("recommend_code");


                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        Log.d("member_usercode", " fail" + message);

                    }
                });
    }

    public static void getpaymenturl() {
        String account = MemberBean.member_id;
        String pwd = MemberBean.member_pwd;
        ApiConUtils.payment_url(ApiUrl.API_URL, ApiUrl.get_payment_url, account, pwd, new ApiConUtils.OnConnect() {
            @Override
            public void onSuccess(String jsonString) throws JSONException {
                Log.d(" MemberBean.payment_url", " 三小?" + jsonString);
                MemberBean.payment_url = jsonString;
            }

            @Override
            public void onFailure(String message) {
                Log.d("member_usercode", " fail" + message);

            }
        });
    }
}