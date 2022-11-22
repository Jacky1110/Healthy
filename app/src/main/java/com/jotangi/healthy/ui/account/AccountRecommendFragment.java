package com.jotangi.healthy.ui.account;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.jotangi.baseutils.DialogUtils;
import com.jotangi.healthy.HpayMall.ApiUrl;
import com.jotangi.healthy.HpayMall.CustomDaialog;
import com.jotangi.healthy.HpayMall.MemberBean;
import com.jotangi.healthy.HpayMall.OrderBean;
import com.jotangi.healthy.R;
import com.jotangi.healthy.ui.ProjScrollViewFragment;
import com.jotangi.jotangi2021.AndroidModule.Api.ApiConUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AccountRecommendFragment extends ProjScrollViewFragment {

    private int inviteFriends = 4;
    TextView tvCode;
    TextView tvRule;
    TextView bnInviteFriend;
    TextView bnFriendList;
    TextView bnCopy;

    public static AccountRecommendFragment newInstance() {
        AccountRecommendFragment fragment = new AccountRecommendFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.recommend_code;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = (ScrollView) inflater.inflate(R.layout.fragment_recommend_code, container, false);
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
       recommmend_List();
        tvRule = rootView.findViewById(R.id.tv_recomment_rule);
        tvRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_ACCOUNT_RECOMMEND_TO_RULE, null);
                }
            }
        });

        tvCode = rootView.findViewById(R.id.tv_code);
        tvCode.setText(MemberBean.member_id);
        bnInviteFriend = rootView.findViewById(R.id.tv_invite_friend);
        bnInviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = tvCode.getText().toString();
                String msg = getString(R.string.invite_friend_message, MemberBean.member_name, code);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        bnFriendList = rootView.findViewById(R.id.tv_friend_list);

        bnFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_ACCOUNT_RECOMMEND_TO_FRIENDS, null);
                }
            }
        });
        bnFriendList.setText(getString(R.string.people_count, inviteFriends));

        bnCopy = rootView.findViewById(R.id.tv_copy);
        bnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = tvCode.getText().toString();
                copyCodeToClipboard(data);
                Toast.makeText(
                        getContext(),
                        "推薦碼"
                                + data + "已複製",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void copyCodeToClipboard(String data) {
        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData;

        clipData = ClipData.newPlainText("text", data);
        clipboardManager.setPrimaryClip(clipData);
        for (int i = 0; i < clipData.getItemCount(); i++) {
            String cv=clipData.getItemAt(i).getText().toString();
            Log.d("user copy data"," : "+cv);
        }
        Log.e("copy data", " : " + clipData.getItemAt(0).getText().toString());


    }
    private void recommmend_List() {
        String id = MemberBean.member_id;
        String pwd = MemberBean.member_pwd;

        ApiConUtils.recommand_list(ApiUrl.API_URL, ApiUrl.recommend_list, id, pwd, new ApiConUtils.OnConnect() {
            @Override
            public void onSuccess(String jsonString) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<String> recommendArrayList = new ArrayList<>();

                        //{"status":"false","code":"0x0201","responseMessage":"ID or code is wrong!"}
                        try {
                            JSONObject jsonObject = new JSONObject(jsonString);
                            Log.e(TAG, jsonObject.toString());
                            String status = jsonObject.getString("status");
                            String code = jsonObject.getString("code");
                            String responseMessage = jsonObject.getString("responseMessage");
                            OrderBean.rcCount=responseMessage;
                            if(code.equals("0x0200"))
                            {
                                bnFriendList.setText(responseMessage+"位");

                            }
                            else {
                                bnFriendList.setText("目前無推薦人");

                            }
                            String recommend_list = jsonObject.getString("recommend_list");


                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CustomDaialog.showNormal(requireActivity(), "", "註冊失敗", "確定", new CustomDaialog.OnBtnClickListener() {
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
//                        DialogUtils.showMyDialog(rootView, 0, R.string.member_register_false, "", R.string.button_confirm, new DialogUtils.OneButtonClickListener() {
//                            @Override
//                            public void onButton1Clicked() {
//                                return;
//                            }
//                        });
                    }
                });
            }
        });
    }

}