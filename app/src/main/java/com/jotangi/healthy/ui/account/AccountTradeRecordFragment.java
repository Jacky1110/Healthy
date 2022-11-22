package com.jotangi.healthy.ui.account;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.healthy.HpayMall.ApiUrl;
import com.jotangi.healthy.HpayMall.MemberBean;
import com.jotangi.healthy.HpayMall.OrderBean;
import com.jotangi.healthy.R;
import com.jotangi.healthy.datamodel.AccountTradeRecord;
import com.jotangi.healthy.ui.ProjConstraintFragment;
import com.jotangi.jotangi2021.AndroidModule.Api.ApiConUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AccountTradeRecordFragment extends ProjConstraintFragment {
    private final String TAG = this.getClass().getSimpleName();

    private List<AccountTradeRecord> recordlist;

    private AccountTradeRecordAdaptor tradeRecordAdaptor;
    protected RecyclerView.LayoutManager tradeLayoutManager;

    ConstraintLayout noneDataView;
    RecyclerView rvDataView;

    public static AccountTradeRecordFragment newInstance() {
        AccountTradeRecordFragment fragment = new AccountTradeRecordFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.account_listitem_record;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_account_trade_record, container, false);
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();

        noneDataView = rootView.findViewById(R.id.layout_nodata);
        rvDataView = rootView.findViewById(R.id.rv_data);

        tradeLayoutManager = new LinearLayoutManager(getContext());
        rvDataView.setLayoutManager(tradeLayoutManager);
        tradeRecordAdaptor = new AccountTradeRecordAdaptor();
        rvDataView.setAdapter(tradeRecordAdaptor);
        rvDataView.scrollToPosition(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    private void initDataSet() {
        if (recordlist == null)
            recordlist = new ArrayList<>();
        else recordlist.clear();

        // for local test
//         recordlist.add(new AccountTradeRecord("松山診所", "110年8月22日 10:00", 50));
//          recordlist.add(new AccountTradeRecord("松山診所", "110年8月29日 10:00", 150));
//         recordlist.add(new AccountTradeRecord("台北診所", "110年9月22日 16:00", 1000));

    }

    private void updateData() {
        initDataSet();
        getlist();
        tradeRecordAdaptor.setDatalist(recordlist);
        noneDataView.setVisibility(recordlist.size() > 0 ? View.INVISIBLE : View.VISIBLE);
    }

    //FIXME KELLY  2021/10/27 ADD   SID REMEMBER CHANGE
    ArrayList<OrderBean> orderBeans;
    OrderBean orderdata;

    private void getlist() {
        sixMonthCount();
        String account = MemberBean.member_id;
        String pwd = MemberBean.member_pwd;
         ApiConUtils.order_list(ApiUrl.API_URL, ApiUrl.order_list, account, pwd, startdate, enddate, new ApiConUtils.OnConnect() {
            @Override
            public void onSuccess(String jsonString) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<String> orderArrayList = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(jsonString);
                            Log.e("Kelly find you   ", jsonArray.toString());
                            orderBeans = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = (JSONObject) jsonArray.get(i);
                                String s1 = jo.getString("store_name");
                                String s2 = jo.getString("order_date");
                                String s3 = jo.getString("order_pay");
                                String s4 = jo.getString("pay_status");
                                if (s4.equals("1")) {

                                    orderArrayList.add(s1);
                                    orderArrayList.add(s2);
//                                    orderArrayList.add(s3);
                                    orderArrayList.add("-" + s3 + "(已退款)");

                                    recordlist.add(new AccountTradeRecord(s1, s2, ("$" + s3)));
                                    String[] str = new String[orderArrayList.size()];
                                    for (int k = 0; k < orderArrayList.size(); k++) {
                                        str[k] = orderArrayList.get(k);

                                    }
                                } else if (s4.equals("9")) {
                                    orderArrayList.add(s1);
                                    orderArrayList.add(s2);
                                    orderArrayList.add("$-" + s3 + "(已退款)");
                                    recordlist.add(new AccountTradeRecord(s1, s2, "$-" + s3 + "(已退款)"));

                                }

                                /*fixme 0==waiting line call back，
                                   1==>pay successfaul，
                                   -1==>pay fail，
                                   9==>退款
                                *  */
                                //pay_status=0  waiting line call back|1==> pay successfaul|-1==>pay fail

                            }

                            tradeRecordAdaptor.setDatalist(recordlist);
                            noneDataView.setVisibility(recordlist.size() > 0 ? View.INVISIBLE : View.VISIBLE);
                            Log.d(TAG, "test" + orderArrayList);


                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }


                    }
                });

            }

            @Override
            public void onFailure(String message) {
                Log.d("member_usercode", " fail" + message);

            }
        });
    }
    String startdate;
    String enddate;
    private void sixMonthCount() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -6);
        Calendar calendarnow = Calendar.getInstance();
        Date date = calendar.getTime();
        Date now=calendarnow.getTime();
        SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd");
         startdate= format.format(date);
         enddate = format.format(now);
    }
}