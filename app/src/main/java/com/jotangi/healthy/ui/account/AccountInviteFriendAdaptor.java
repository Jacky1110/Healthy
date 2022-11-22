package com.jotangi.healthy.ui.account;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.accountutils.datamodel.InviteFriendRecord;
import com.jotangi.healthy.R;

import com.jotangi.healthy.ui.customview.InviteFriendItemView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AccountInviteFriendAdaptor extends RecyclerView.Adapter<AccountInviteFriendAdaptor.MyViewHolder> {
    private final String TAG = this.getClass().getSimpleName();

    private List<InviteFriendRecord> datalist;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(InviteFriendItemView v) {
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
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        InviteFriendItemView v = new InviteFriendItemView(parent.getContext());
        ViewGroup.MarginLayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int hm = parent.getResources().getDimensionPixelSize(R.dimen.itemview_trade_record_horizontal_margin);
        int vm = parent.getResources().getDimensionPixelSize(R.dimen.itemview_trade_record_vertical_margin);
        lp.setMargins(hm, vm, hm, vm);
        v.setLayoutParams(lp);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        if (position < datalist.size()) {
            InviteFriendRecord trade = datalist.get(position);
            InviteFriendItemView rootview = (InviteFriendItemView) holder.itemView;
            rootview.setData(trade);
        }
    }

    public AccountInviteFriendAdaptor() {
        datalist = new ArrayList<>();
    }

    public AccountInviteFriendAdaptor(List<InviteFriendRecord> datalist) {
        this();
        this.datalist.addAll(datalist);
    }

    public void setDatalist(List<InviteFriendRecord> datalist) {
        this.datalist.clear();
        this.datalist.addAll(datalist);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datalist == null ? 0 : datalist.size();
    }

}
