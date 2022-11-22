package com.jotangi.healthy.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jotangi.healthy.R;
import com.jotangi.healthy.datamodel.AccountTradeRecord;

import org.jetbrains.annotations.NotNull;

public class AccountTradeRecordItemView extends ConstraintLayout {
    private final String TAG = this.getClass().getSimpleName();
    private ConstraintLayout rootView;
    private TextView nameView;
    private TextView dateTimeView;
    private TextView costView;

    public AccountTradeRecordItemView(@NonNull @NotNull Context context) {
        super(context);
        init(null, 0);
    }

    public AccountTradeRecordItemView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public AccountTradeRecordItemView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public AccountTradeRecordItemView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        rootView = (ConstraintLayout)LayoutInflater.from(getContext()).inflate(R.layout.itemview_account_trade_record, this);
        nameView = rootView.findViewById(R.id.tv_name);
        dateTimeView = rootView.findViewById(R.id.tv_datetime);
        costView = rootView.findViewById(R.id.tv_cost);
    }

    public void setData(AccountTradeRecord trade) {
        nameView.setText(trade.getClinicName());
        dateTimeView.setText(trade.getTradeTime());
        costView.setText(trade.getCost());
    }
}
