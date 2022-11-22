package com.jotangi.healthy.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jotangi.healthy.HpayMall.CustomDaialog;
import com.jotangi.healthy.R;
import com.jotangi.healthy.ui.ProjConstraintFragment;

public class AccountTradeMainFragment extends ProjConstraintFragment {

    TextView tvTrade;
    TextView tvOrder;
    public static AccountTradeMainFragment newInstance() {
        AccountTradeMainFragment fragment = new AccountTradeMainFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.account_listitem_record;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = (ConstraintLayout)inflater.inflate(R.layout.fragment_trade_main, container, false);
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();

        tvTrade = rootView.findViewById(R.id.tv_trade);
        tvTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_ACCOUNT_TRADE_TO_RECORD, null);
                }
            }
        });
        tvOrder = rootView.findViewById(R.id.tv_order);
        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_ACCOUNT_TRADE_TO_ORDER, null);
                }
//                requireActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        CustomDaialog.showNormal(requireActivity(), "", "Coming Soon", "確定", new CustomDaialog.OnBtnClickListener() {
//                            @Override
//                            public void onCheck() {
//
//                            }
//
//                            @Override
//                            public void onCancel() {
//                                CustomDaialog.closeDialog();
//
//                            }
//                        });
//                    }
//                });
            }
        });
    }
}