package com.jotangi.healthy.ui.account;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.accountutils.datamodel.PointTradeRecord;
import com.jotangi.healthy.ui.customview.AccountPointRecordItemView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AccountPointRecordAdaptor extends RecyclerView.Adapter<AccountPointRecordAdaptor.ItemViewHolder> {
    private final String TAG = this.getClass().getSimpleName();

    private List<PointTradeRecord> datalist;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(AccountPointRecordItemView v) {
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
    public ItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        AccountPointRecordItemView v = new AccountPointRecordItemView(parent.getContext());
        ViewGroup.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemViewHolder holder, int position) {
        if (position < datalist.size()) {
            PointTradeRecord trade = datalist.get(position);
            AccountPointRecordItemView rootview = (AccountPointRecordItemView) holder.itemView;
            rootview.setData(trade);
        }
    }

    public AccountPointRecordAdaptor() {
        datalist = new ArrayList<>();
    }

    public AccountPointRecordAdaptor(List<PointTradeRecord> datalist) {
        this();
        this.datalist.addAll(datalist);
    }

    public void setDatalist(List<PointTradeRecord> datalist) {
        this.datalist.clear();
        this.datalist.addAll(datalist);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datalist == null ? 0 : datalist.size();
    }

}
