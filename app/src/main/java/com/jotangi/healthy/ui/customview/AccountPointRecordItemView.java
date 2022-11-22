package com.jotangi.healthy.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jotangi.healthy.R;
import com.jotangi.accountutils.datamodel.PointTradeRecord;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AccountPointRecordItemView extends ConstraintLayout {
    private final String TAG = this.getClass().getSimpleName();

    private Map<Integer, Integer> typeMap;

    private ConstraintLayout rootView;
    private TextView typeView;
    private TextView dateView;
    private TextView pointView;

    public AccountPointRecordItemView(@NonNull @NotNull Context context) {
        super(context);
        init(null, 0);
    }

    public AccountPointRecordItemView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public AccountPointRecordItemView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public AccountPointRecordItemView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        initPointTypeMap();
        rootView = (ConstraintLayout)LayoutInflater.from(getContext()).inflate(R.layout.itemview_account_point_record, this);
        typeView = rootView.findViewById(R.id.tv_type);
        dateView = rootView.findViewById(R.id.tv_date);
        pointView = rootView.findViewById(R.id.tv_point);
    }

    private void initPointTypeMap() {
        typeMap = new HashMap<>();
        typeMap.put(PointTradeRecord.POINT_GET, R.string.point_get);
        typeMap.put(PointTradeRecord.POINT_CANCEL, R.string.point_cancel);
        typeMap.put(PointTradeRecord.POINT_USED, R.string.point_used);
    }

    private String getStringByType(int type) {
        try {
            if (typeMap != null && typeMap.containsKey(type)) {
                int typeid = typeMap.get(type);
                return getResources().getString(typeid);
            } else {
                return getResources().getString(R.string.err_point_type,type);
            }
        } catch (Exception ex) {
            return getResources().getString(R.string.err_exception);
        }
    }

    public void setData(PointTradeRecord record) {
        typeView.setText(getStringByType(record.getPointType()));
        dateView.setText(record.getRecordTime());
        pointView.setText(getResources().getString(R.string.point_value, record.getPointCount()));
    }
}
