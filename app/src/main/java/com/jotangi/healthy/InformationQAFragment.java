package com.jotangi.healthy;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jotangi.healthy.HpayMall.Mall.SCartFragmemt;
import com.jotangi.healthy.ui.ProjConstraintFragment;
import com.jotangi.healthy.ui.account.AccountMainFragment;
import com.jotangi.healthy.ui.account.AccountTradeRecordFragment;
import com.jotangi.healthy.ui.home.HomeMainFragment;


public class InformationQAFragment extends ProjConstraintFragment {


    private Button btnIs;
    private TextView txtBuy;

    public static InformationQAFragment newInstance() {
        InformationQAFragment fragment = new InformationQAFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_information_q_a, container, false);
        initView();
        return rootView;
    }

    private void initView() {
        btnIs = rootView.findViewById(R.id.btnIs);
        btnIs.setOnClickListener(v -> {
            FragmentTransaction transaction=
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction();
            transaction.replace(R.id.nav_host_fragment_activity_main, AccountMainFragment.newInstance());
            transaction.commit();
        });

        txtBuy = rootView.findViewById(R.id.tv_buy);
        txtBuy.setMovementMethod(ScrollingMovementMethod.getInstance());
    }


    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.account_qa;
    }
}