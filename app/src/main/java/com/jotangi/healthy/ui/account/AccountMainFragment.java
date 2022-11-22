package com.jotangi.healthy.ui.account;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import com.jotangi.baseutils.FileUtils;
import com.jotangi.healthy.HpayMall.ApiUrl;
import com.jotangi.healthy.HpayMall.CustomDaialog;
import com.jotangi.healthy.HpayMall.MemberBean;
import com.jotangi.healthy.R;
import com.jotangi.healthy.ui.ProjConstraintFragment;
import com.jotangi.healthy.utils.ProjSharePreference;
import com.jotangi.jotangi2021.AndroidModule.Api.ApiConUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * ♡♡♡♡♡♡♡♡♡♡♡Kelly 's Note♡♡♡♡♡♡´ސު｀
 * ====================================================
 * 如果登入過會直接進這個page
 * Sean 判斷的function在   display.....
 * 儲存會員資料的我用save....的那個
 */
public class AccountMainFragment extends ProjConstraintFragment {

    private RelativeLayout bnAccountData;
    private RelativeLayout bnRecord;
    private RelativeLayout bnPoint;
    private RelativeLayout bnUserRule;
    private RelativeLayout bnRecommend;
    private RelativeLayout bnCoupon;
    private RelativeLayout bnQA;
    private RelativeLayout bnLogout;
    private RelativeLayout bnMemberDelete;
    private CardView vwUserCard;
    private ImageView vwHeadImage;
    private ImageButton btncart;
    private Boolean isLogin = false;
    TextView tvName, tvPoint;
    Boolean isCheck = false;

    public static AccountMainFragment newInstance() {
        AccountMainFragment fragment = new AccountMainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.title_account;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        updateHeadPhoto();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_account_main, container, false);
//        if (MemberBean.member_phone == null || MemberBean.member_pwd == null) {
//            fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_LOGIN, null);
//
//        } else {
//            compare();
//
//        }
//        compare();
        handleLogin();
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        tvName = rootView.findViewById(R.id.txt_memberName);
        tvPoint = rootView.findViewById(R.id.tv_point_value);
        tvPoint.setText(MemberBean.member_totalpoints);
        tvName.setText(MemberBean.member_name);
        btncart = getActivity().findViewById(R.id.btn_car);
        btncart.setVisibility(View.VISIBLE);

        bnAccountData = rootView.findViewById(R.id.item_account);
        bnAccountData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogin = ProjSharePreference.getLoginState(requireContext());
                if (isLogin) {
                    if (fragmentListener != null) {
                        fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_DATA, null);
                        member_information();
                    }
                }
            }
        });

        bnRecord = rootView.findViewById(R.id.item_record);
        bnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_TRADE_MAIN, null);
                }
            }
        });

        bnPoint = rootView.findViewById(R.id.item_point);
        bnPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_POINT, null);
                }
            }
        });

        bnUserRule = rootView.findViewById(R.id.item_rule);
        bnUserRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_USERRULE, null);
                }
            }
        });

        bnRecommend = rootView.findViewById(R.id.item_recommend);
        bnRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_RECOMMEND, null);
                }
            }
        });

        bnCoupon = rootView.findViewById(R.id.item_coupon);
        bnCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_COUPON_INDEX, null);
                }
            }
        });

        bnQA = rootView.findViewById(R.id.item_qa);
        bnQA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_QA, null);
                }
            }
        });

        bnLogout = rootView.findViewById(R.id.bn_logout);
        bnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {
                    MemberBean.member_id = "";
                    MemberBean.member_pwd = "";
                    savaTermsStatus(true, "111");
                    savaLoginStatus(false, MemberBean.member_id, MemberBean.member_pwd);
                    ProjSharePreference.setLoginState(getContext(), false);
                    Log.d(TAG, "Memberid: " + MemberBean.member_id);
                    fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_LOGIN, null);
                }
            }
        });

        vwUserCard = rootView.findViewById(R.id.cv_user);
        vwUserCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_ACCOUNT_MAIN_USER_HEAD_CLICKED, null);
                }
            }
        });

        bnMemberDelete = rootView.findViewById(R.id.item_member_delete);
        bnMemberDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("提示")
                        .setIconAttribute(android.R.attr.alertDialogIcon)
                        .setMessage("確定要刪除帳號嗎? ")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                memberDelete();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create().show();

            }
        });

        vwHeadImage = rootView.findViewById(R.id.iv_head);
    }

    private void handleLogin() {

        isLogin = ProjSharePreference.getLoginState(requireContext());

        if (!isLogin) {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment_activity_main, AccountLoginFragment.newInstance());
            transaction.commit();
        } else {

        }
    }

    private void savaTermsStatus(boolean status, String code) {
        SharedPreferences pref = requireActivity().getSharedPreferences("UserTerms", MODE_PRIVATE);
        pref.edit()
                .putBoolean("Terms", status)
                .putString("code", code)
                .commit();
    }

    private void updateHeadPhoto() {
        File imagefile = FileUtils.getImageFile(requireContext(), FileUtils.USER_HEAD_PHOTO_FILE);
        try {
            assert imagefile != null;

            Bitmap bitmap = BitmapFactory.decodeFile(imagefile.getAbsolutePath());
            vwHeadImage.setImageBitmap(bitmap);
//            UserPic(imagefile.getAbsoluteFile());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
     *
     *
     * 使用者條款強制更新部分
     * 去R.string.rule_msg
     * 把001改002 ||  002改001
     * 但是111是登出專用的 VALUE
     * */
    private void compare() {
        String terms = getString(R.string.rule_msg);
        String test1 = terms.substring(0, 4);
        SharedPreferences isGetLogin = getActivity().getSharedPreferences("UserTerms", MODE_PRIVATE);
        boolean termsresult = isGetLogin.getBoolean("Terms", false);
        String termsCode = isGetLogin.getString("code", "");
        if (!test1.equals(termsCode) || termsCode.equals("111")) {
            CustomDaialog.showMyDialog(getActivity(), "使用者條款更新通知", "條款已更新，麻煩您詳閱一遍", "確定", new CustomDaialog.OnBtnClickListener() {
                @Override
                public void onCheck() {
                    if (fragmentListener != null) {
                        CustomDaialog.check();
                        fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_USERRULE, "1");
                        savaTermsStatus(true, test1);
                    }

                    isCheck = true;
                }

                @Override
                public void onCancel() {
                    if (isCheck) {
                        CustomDaialog.closeDialog();
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                CustomDaialog.showNormal(requireActivity(), "", "還未閱讀使用者條款", "確定", new CustomDaialog.OnBtnClickListener() {
                                    @Override
                                    public void onCheck() {

                                    }

                                    @Override
                                    public void onCancel() {

                                        // fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_LOGIN, null);
                                        CustomDaialog.closeDialog();

                                    }
                                });
                                return;
                            }
                        });
//                        DialogUtils.showMyDialog(rootView, "提醒事項", "還未閱讀使用者條款", "確定", new DialogUtils.OneButtonClickListener() {
//                            @Override
//                            public void onButton1Clicked() {
//                                return;
//                            }
//                        });
                    }
                }
            });

        }
    }

    private void member_information() {
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
                        JSONArray jsonArray = new JSONArray(jsonString);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
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
                }
        );
    }

    private void savaLoginStatus(boolean status, String account, String pwd) {
        SharedPreferences pref = requireActivity().getSharedPreferences("loginInfo", MODE_PRIVATE);
        pref.edit()
                .putBoolean("isLogin", status)
                .putString("account", account)
                .putString("password", pwd)
                .apply();
        Log.d("登出", "成功" + status + "/" + account + "/" + pwd);
    }

    /*
     *更新大頭貼照片
     *測試可以上傳，但是目前pm說可以先不用，所以註解
     * */
    private void UserPic(File imgPic) {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("upload_filename", imgPic.getName(), RequestBody.create(imgPic, MediaType.parse("image/jpg")))
                .addFormDataPart("member_id", MemberBean.member_id)
                .addFormDataPart("member_pwd", MemberBean.member_pwd)
                .build();
        Request request = new Request.Builder()
                .url("http://211.20.185.2/medicalec/api/user_uploadpic.php").post(body).build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException {
                String banner = response.body().string();
                Log.d("會員大頭貼", "照片更新成功" + banner);
            }

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
//                Log.d("連線", e.getStackTrace().toString());
            }
        });
    }

    //member_delete
    private void memberDelete() {
        ApiConUtils.member_delete(
                ApiUrl.API_URL,
                ApiUrl.user_delete,
                MemberBean.member_id,
                MemberBean.member_pwd,
                new ApiConUtils.OnConnect() {
                    @Override
                    public void onSuccess(String jsonString) throws JSONException {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        String status = jsonObject.getString("status");
                        String code = jsonObject.getString("code");

                        try {
                            if (code.equals("0x0200")) {
                                savaTermsStatus(true, "111");
                                savaLoginStatus(false, "", "");
                                ProjSharePreference.setLoginState(getContext(), false);
                                fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_LOGIN, null);
                            } else {
                                Toast.makeText(
                                        requireContext(),
                                        "刪除失敗，請稍後再試",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                }
        );
    }
}