package com.jotangi.healthy.ui.account;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.healthy.R;
import com.jotangi.healthy.datamodel.AccountTradeRecord;
import com.jotangi.healthy.ui.customview.AccountTradeRecordItemView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AccountTradeRecordAdaptor extends RecyclerView.Adapter<AccountTradeRecordAdaptor.TradeViewHolder> {
    private final String TAG = this.getClass().getSimpleName();

    private List<AccountTradeRecord> datalist;

    public static class TradeViewHolder extends RecyclerView.ViewHolder {

        public TradeViewHolder(AccountTradeRecordItemView v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
        }

    }

    @NonNull
    @NotNull
    @Override
    public TradeViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        AccountTradeRecordItemView v = new AccountTradeRecordItemView(parent.getContext());
        ViewGroup.MarginLayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int hm = parent.getResources().getDimensionPixelSize(R.dimen.itemview_trade_record_horizontal_margin);
        int vm = parent.getResources().getDimensionPixelSize(R.dimen.itemview_trade_record_vertical_margin);
        lp.setMargins(hm, vm, hm, vm);
        v.setLayoutParams(lp);
        return new TradeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TradeViewHolder holder, int position) {
        if (position < datalist.size()) {
            AccountTradeRecord trade = datalist.get(position);
            AccountTradeRecordItemView rootview = (AccountTradeRecordItemView) holder.itemView;
            rootview.setData(trade);
        }
    }

    public AccountTradeRecordAdaptor() {
        datalist = new ArrayList<>();
    }

    public AccountTradeRecordAdaptor(List<AccountTradeRecord> datalist) {
        this();
        this.datalist.addAll(datalist);
    }

    public void setDatalist(List<AccountTradeRecord> datalist) {
        this.datalist.clear();
        this.datalist.addAll(datalist);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datalist == null ? 0 : datalist.size();
    }

}
