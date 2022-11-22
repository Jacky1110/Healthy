package com.jotangi.healthy.ui.account;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.baseutils.DialogUtils;
import com.jotangi.healthy.HpayMall.ApiUrl;
import com.jotangi.healthy.HpayMall.CustomDaialog;
import com.jotangi.healthy.HpayMall.MemberBean;
import com.jotangi.healthy.HpayMall.OrderBean;
import com.jotangi.healthy.R;
import com.jotangi.accountutils.datamodel.InviteFriendRecord;
import com.jotangi.healthy.ui.ProjConstraintFragment;
import com.jotangi.jotangi2021.AndroidModule.Api.ApiConUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AccountInviteFriendFragment extends ProjConstraintFragment {
    private final String TAG = this.getClass().getSimpleName();

    private List<InviteFriendRecord> recordlist;

    private AccountInviteFriendAdaptor recordAdaptor;
    protected RecyclerView.LayoutManager tradeLayoutManager;

    TextView noneDataView;
    RecyclerView rvDataView;

    public static AccountInviteFriendFragment newInstance() {
        AccountInviteFriendFragment fragment = new AccountInviteFriendFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.recommend_record;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_account_invite_friend, container, false);
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
recommmend_List();

        noneDataView = rootView.findViewById(R.id.tv_recommend_none);

        rvDataView = rootView.findViewById(R.id.rv_data);

        tradeLayoutManager = new LinearLayoutManager(getContext());
        rvDataView.setLayoutManager(tradeLayoutManager);
        recordAdaptor = new AccountInviteFriendAdaptor();
        rvDataView.setAdapter(recordAdaptor);
        rvDataView.scrollToPosition(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    private void initDataSet() {

        if (recordlist == null) recordlist = new ArrayList<>();
        else recordlist.clear();

        // for local test
//        recordlist.add(new InviteFriendRecord(1, "健康point", 100, "110年8月22日 10:00", "0921345678"));
//        recordlist.add(new InviteFriendRecord(1, "健康point", 100, "110年8月22日 10:00", "0923111111"));
//        recordlist.add(new InviteFriendRecord(1, "健康point", 100, "110年8月22日 10:00", "0926333333"));
//        recordlist.add(new InviteFriendRecord(1, "健康point", 100, "110年8月22日 10:00", "0928222222"));

    }

    private void updateData() {
        initDataSet();
        recommmend_List();
        recordAdaptor.setDatalist(recordlist);
        noneDataView.setVisibility(recordlist.size() > 0 ? View.INVISIBLE : View.VISIBLE);
    }

    ArrayList<OrderBean> recommandBeans;

    private void recommmend_List() {
        String id = MemberBean.member_id;
        String pwd = MemberBean.member_pwd;

        ApiConUtils.recommand_list(ApiUrl.API_URL, ApiUrl.recommend_list, id, pwd, new ApiConUtils.OnConnect() {
            @Override
            public void onSuccess(String jsonString) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recordlist.clear();
                        ArrayList<String> recommendArrayList = new ArrayList<>();

                        //{"status":"false","code":"0x0201","responseMessage":"ID or code is wrong!"}
                        try {
                            JSONObject jsonObject = new JSONObject(jsonString);
                            Log.e(TAG, jsonObject.toString());
                            String status = jsonObject.getString("status");
                            String code = jsonObject.getString("code");
                            String responseMessage = jsonObject.getString("responseMessage");
                            OrderBean.rcCount=responseMessage;
                            String recommend_list = jsonObject.getString("recommend_list");
                            JSONArray jsonArray = new JSONArray(recommend_list);
                            recommandBeans = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = (JSONObject) jsonArray.get(i);
                                String s1 = jo.getString("recommend_code");
                                String s2 = jo.getString("recommend_date");
                                String s3 = jo.getString("recommend_member");

                                recordlist.add(new InviteFriendRecord(1, "健康point", 100, s2, s3));

//                                if (responseMessage.equals("1")) {
//                                    recommendArrayList.add(s1);
//                                    recommendArrayList.add(s2);
//                                    recommendArrayList.add(s3);
//                                    recordlist.add(new InviteFriendRecord(1, "健康point", 100, s2, s3));
//                                }

                            }
                            recordAdaptor.setDatalist(recordlist);
                            noneDataView.setVisibility(recordlist.size() > 0 ? View.INVISIBLE : View.VISIBLE);
                            Log.d(TAG, "test" + recordAdaptor);


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
                        CustomDaialog.showNormal(requireActivity(), "", "推薦失敗", "確定", new CustomDaialog.OnBtnClickListener() {
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