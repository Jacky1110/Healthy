package com.jotangi.healthy.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jotangi.healthy.HpayMall.ApiUrl;
import com.jotangi.healthy.HpayMall.CustomDaialog;
import com.jotangi.healthy.HpayMall.MemberBean;
import com.jotangi.healthy.R;
import com.jotangi.healthy.ui.ProjConstraintFragment;
import com.jotangi.healthy.utils.ProjSharePreference;
import com.jotangi.jotangi2021.AndroidModule.Api.ApiConUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeMainFragment extends ProjConstraintFragment {

    View bnScanQRCode;
    TextView tvPoint;
    ImageButton btncart;
    boolean isShow;

    public static HomeMainFragment newInstance() {
        HomeMainFragment fragment = new HomeMainFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.title_home;
        try {
            switch (MemberBean.channel_id) {
                case 1:
                    requireActivity().runOnUiThread(() -> CustomDaialog.showOne(requireActivity(), "掃描支付", "掃描商城折抵", "確定", new CustomDaialog.OnBtnClickListener() {
                        @Override
                        public void onCheck() {
                            CustomDaialog.closeDialog();
                            if (fragmentListener != null) {
                                fragmentListener.onAction(FUNC_SCAN_TO_WEBPAY, null);

                            }
                        }

                        @Override
                        public void onCancel() {
                        }
                    }));
                    break;
                case 2:
                    requireActivity().runOnUiThread(() -> CustomDaialog.showTwo(requireActivity(), "掃描支付", "掃描商城折抵", "確定", new CustomDaialog.OnBtnClickListener() {
                        @Override
                        public void onCheck() {
                            CustomDaialog.closeDialog();
                            if (fragmentListener != null) {
                                fragmentListener.onAction(FUNC_SCAN_TO_WEBPAY, null);

                            }
                        }

                        @Override
                        public void onCancel() {
                            CustomDaialog.closeDialog();
                            if (fragmentListener != null) {
                                /*新增需求:原先直接切到商城，後續增加商城資訊頁面，所以會先跳商城資訊page*/
                                fragmentListener.onAction(FUNC_MAIN_TO_MALL, null);
                                isShow = true;
                            }
                        }
                    }));

                    break;

            }

        } catch (Exception e) {

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onResume()");
        isShow = true;

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onResume()");


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_home_main, container, false);

        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
//        what();
        tvPoint = rootView.findViewById(R.id.tv_point_value);
//        btncart = getActivity().findViewById(R.id.btn_car);
//        if (MemberBean.member_id == null && MemberBean.member_pwd == null) {
//            btncart.setVisibility(View.VISIBLE);
//        } else {
//            btncart.setVisibility(View.VISIBLE);
//        }

        bnScanQRCode = rootView.findViewById(R.id.layout_scan_qrcode);
        Bundle data = getArguments();
        if (data != null) {
            if (data.getString("channel_id").equals("1")) {
                showDialog();
            }
        }
        bnScanQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ProjSharePreference.getLoginState(requireContext())) {
                    if (fragmentListener != null) {
                        /* CameraXPreviewFragment==>掃描barcode */
                        fragmentListener.onAction(FUNC_HOME_TO_SCAN, null);

                    }
                } else {
                    Toast.makeText(
                            requireContext(),
                            "請先登入帳號",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });


    }

    private void showDialog() {
        Bundle channelId = new Bundle();
        channelId.putString("channel_price", "1");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomDaialog.showTwo(requireActivity(), "掃描支付", "掃描商城折抵", "確定", new CustomDaialog.OnBtnClickListener() {
                    @Override
                    public void onCheck() {
                        CustomDaialog.closeDialog();
                        if (fragmentListener != null) {
                            fragmentListener.onAction(FUNC_SCAN_TO_WEBPAY, channelId);
                        }
                    }

                    @Override
                    public void onCancel() {
                        CustomDaialog.closeDialog();
                        if (fragmentListener != null) {
                            /*新增需求:原先直接切到商城，後續增加商城資訊頁面，所以會先跳商城資訊page*/
                            fragmentListener.onAction(FUNC_MAIN_TO_MALL, channelId);
                            isShow = true;
                        }
                    }
                });
            }
        });


    }

    private void what() {
        if (ProjSharePreference.getLoginState(requireContext())) {
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
                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvPoint.setText(MemberBean.member_totalpoints);

                                        }
                                    });
                                }

                            }
                        }

                        @Override
                        public void onFailure(String message) {
                            Log.d("member_usercode", " fail" + message);

                        }
                    });
        }
    }
}