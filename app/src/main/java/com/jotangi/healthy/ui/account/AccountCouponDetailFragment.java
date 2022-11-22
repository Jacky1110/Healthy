package com.jotangi.healthy.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jotangi.healthy.MainActivity;
import com.jotangi.healthy.R;
import com.jotangi.healthy.ui.ProjConstraintFragment;

public class AccountCouponDetailFragment extends ProjConstraintFragment {
    TextView tv_coupon_title, tv_coupon_content;
    ConstraintLayout btn_back, btn_send;
    TextView couponID;

    public static AccountCouponDetailFragment newInstance() {
        AccountCouponDetailFragment fragment = new AccountCouponDetailFragment();
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
        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_account_coupon_detail, container, false);
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        couponID = rootView.findViewById(R.id.couponID);
        tv_coupon_title = rootView.findViewById(R.id.tv_coupon_title);
        tv_coupon_content = rootView.findViewById(R.id.tv_coupon_content);
        btn_back = rootView.findViewById(R.id.btn_back);
        btn_send = rootView.findViewById(R.id.btn_send);
        Bundle data = getArguments();
        if (data != null) {
            /*
             *  tv_coupon_title=coupon_name
             * tv_coupon_content=coupon_description
             *coupinId=coupon_id
             * */
            couponID.setText(data.getString("id"));
            tv_coupon_title.setText(data.getString("title"));
            tv_coupon_content.setText(data.getString("content"));
        }
        btn_back.setOnClickListener(view -> ((MainActivity) getContext()).onBackPressed());
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_HOME_TO_SCAN, null);
                }
            }
        });
    }
}
