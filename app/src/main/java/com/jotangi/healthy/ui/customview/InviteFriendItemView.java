package com.jotangi.healthy.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jotangi.healthy.R;
import com.jotangi.accountutils.datamodel.InviteFriendRecord;

import org.jetbrains.annotations.NotNull;

public class InviteFriendItemView extends ConstraintLayout {
    private final String TAG = this.getClass().getSimpleName();
    private ConstraintLayout rootView;
    private TextView phoneView;
    private TextView dateTimeView;
    private TextView awardView;

    public InviteFriendItemView(@NonNull @NotNull Context context) {
        super(context);
        init(null, 0);
    }

    public InviteFriendItemView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public InviteFriendItemView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public InviteFriendItemView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        rootView = (ConstraintLayout)LayoutInflater.from(getContext()).inflate(R.layout.itemview_invited_friend, this);
        phoneView = rootView.findViewById(R.id.tv_phone_data);
        dateTimeView = rootView.findViewById(R.id.tv_date_data);
        awardView = rootView.findViewById(R.id.tv_award_data);
    }

    public void setData(InviteFriendRecord data) {
        phoneView.setText(data.getPhone());
        dateTimeView.setText(data.getDatetime());
        awardView.setText(getResources().getString(R.string.award_message, data.getAwardPoint(), data.getAwardTypeName()));
    }
}
