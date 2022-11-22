package com.jotangi.healthy.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jotangi.healthy.R;
import com.jotangi.healthy.ui.ProjConstraintFragment;

public class AccountCouponIndexFragment extends ProjConstraintFragment {
    TextView tv_coupon1,tv_coupon2;
    public static AccountCouponIndexFragment newInstance(){
        AccountCouponIndexFragment fragment = new AccountCouponIndexFragment();
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
        rootView = (ConstraintLayout)inflater.inflate(R.layout.fragment_account_coupon_index, container, false);
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        tv_coupon1 = rootView.findViewById(R.id.tv_coupon_1);
        tv_coupon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentListener != null) {
                    Bundle data = new Bundle();
                    data.putInt("type",0);
                    fragmentListener.onAction(FUNC_ACCOUNT_COUPON_TO_COUPON_MAIN, data);
                }
            }
        });
        tv_coupon2 = rootView.findViewById(R.id.tv_coupon_2);
        tv_coupon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentListener != null) {
                    Bundle data = new Bundle();
                    data.putInt("type",1);
                    fragmentListener.onAction(FUNC_ACCOUNT_COUPON_TO_COUPON_MAIN, data);
                }
            }
        });
    }
}
