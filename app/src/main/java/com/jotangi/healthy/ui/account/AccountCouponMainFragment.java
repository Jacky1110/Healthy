package com.jotangi.healthy.ui.account;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.healthy.HpayMall.ApiUrl;
import com.jotangi.healthy.HpayMall.MemberBean;
import com.jotangi.healthy.R;
import com.jotangi.healthy.ui.ProjConstraintFragment;
import com.jotangi.jotangi2021.AndroidModule.Api.ApiConUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/*
*
* =https://tripspottest.jotangi.net/medicalec/payindex.php?sid=132&mid=397
* */
public class AccountCouponMainFragment extends ProjConstraintFragment {
    RecyclerView rv_coupon;
    public static AccountCouponMainFragment newInstance(){
        AccountCouponMainFragment fragment = new AccountCouponMainFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.coupon_0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ConstraintLayout)inflater.inflate(R.layout.fragment_account_coupon_main, container, false);
        return rootView;
    }
    ArrayList data = new ArrayList();
    @Override
    protected void initViews() {
        super.initViews();
        CouponApi();
        rv_coupon = rootView.findViewById(R.id.rv_coupon);
        rv_coupon.setLayoutManager(new LinearLayoutManager(requireContext()));
        int type = 0;
        if(getArguments()!=null){
            type = getArguments().getInt("type");
            Log.d("eee",type+"");
        }
        AccountCouponAdapter adapter = new AccountCouponAdapter();
        adapter.setFragmentListener(fragmentListener);
       data=new ArrayList();
//        for(int i=1;i<=10;i++){
//            CouponModel model = new CouponModel();
//            model.id="id"+i;
//            model.title = "title "+i;
//            model.content = "content "+i;
//            data.add(model);
//        }
        rv_coupon.setAdapter(adapter);
        if(type == 0)

            adapter.setData(data); //call api
    }
    private  void CouponApi()
    {
        String id=MemberBean.member_id;
        String pwd=MemberBean.member_pwd;
        ApiConUtils.coupon_list(ApiUrl.API_URL, ApiUrl.coupon_list,id ,pwd , new ApiConUtils.OnConnect() {
            @Override
            public void onSuccess(String jsonString) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonString);
                            String status = jsonObject.getString("status");
                            String code = jsonObject.getString("code");
                            String responseMessage = jsonObject.getString("responseMessage");
                            String mycoupon_list = jsonObject.getString("mycoupon_list");
                            JSONArray couArray=new JSONArray(mycoupon_list);
                            for (int i = 0; i < couArray.length(); i++) {
                                JSONObject jo = (JSONObject) couArray.get(i);
                                String s1 = jo.getString("coupon_id");
                                String s2 = jo.getString("coupon_name");
                                String s3 = jo.getString("coupon_description");

                              //  recordlist.add(new PointTradeRecord(PointTradeRecord.POINT_GET, s2, Integer.valueOf(s3), s2));

//                                if (responseMessage.equals("1")) {
//                                    recommendArrayList.add(s1);
//                                    recommendArrayList.add(s2);
//                                    recommendArrayList.add(s3);
//                                    recordlist.add(new InviteFriendRecord(1, "健康point", 100, s2, s3));
//                                }

                            }

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

                    }
                });

            }
        });
    }
}
