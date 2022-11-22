package com.jotangi.healthy.HpayMall;

import java.util.List;

public class OrderBean {
    private List<OrderBean> orderBeanList;
    public static String oid;
    public static String order_no;
    public static String order_date;
    public static String store_id;
    public static String member_id;

    public static String order_amount;
    public static String coupon_no;
    public static String discount_amount;
    public static String pay_type;
    public static String order_pay;
    public static String pay_status;
    public static String bonus_point;
    public static String order_status;

    /*
     *"recommend_list": [
    {
      "0": "0965026589",
      "1": "2021-12-03",
      "2": "12342234",
      "recommend_code": "0965026589",
      "recommend_date": "2021-12-03",
      "recommend_member": "12342234"
    }
  ]
     *
     * */
    public static String rcCount;
    public static String recommend_code;
    public static String recommend_date;
    public static String recommend_member;

    public String getrcCount() {
        return rcCount;
    }

    public void setrcCount(String rcCount) {
        this.rcCount = rcCount;
    }

    public String getrecommend_member() {
        return recommend_member;
    }

    public void setrecommend_member(String recommend_member) {
        this.recommend_member = recommend_member;
    }

    public String getrecommend_date() {
        return recommend_date;
    }

    public void setrecommend_date(String recommend_date) {
        this.recommend_date = recommend_date;
    }

    public String getrecommend_code() {
        return recommend_code;
    }

    public void setrecommend_code(String recommend_code) {
        this.recommend_code = recommend_code;
    }

    /*
     *
     *
     * */
    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getorder_no() {
        return order_no;
    }

    public void setorder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getorder_date() {
        return order_date;
    }

    public void setorder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getstore_id() {
        return store_id;
    }

    public void setstore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getmember_id() {
        return member_id;
    }

    public void setmember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getorder_amount() {
        return order_amount;
    }

    public void setorder_amount(String order_amount) {
        this.order_amount = order_amount;
    }


    public String getcoupon_no() {
        return coupon_no;
    }

    public void setcoupon_no(String coupon_no) {
        this.coupon_no = coupon_no;
    }

    public String getdiscount_amount() {
        return discount_amount;
    }

    public void setdiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getpay_type() {
        return pay_type;
    }

    public void setpay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getorder_pay() {
        return order_pay;
    }

    public void setorder_pay(String order_pay) {
        this.order_pay = order_pay;
    }

    public String getpay_status() {
        return pay_status;
    }

    public void setpay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getbonus_point() {
        return bonus_point;
    }

    public void setbonus_point(String bonus_point) {
        this.bonus_point = bonus_point;
    }

    public String getorder_status() {
        return order_status;
    }

    public void setorder_status(String order_status) {
        this.order_status = order_status;
    }

    public List<OrderBean> getorderBeanList() {
        return orderBeanList;
    }

    public void setBanner_list(List<OrderBean> orderBeanList) {
        this.orderBeanList = orderBeanList;
    }
    /*
    * [
  {
    "0": "65",
    "1": "1",
    "2": "0931259760",
    "3": "2021-12-03 11:07:18",
    "4": "3",
    "5": "100",
    "bid": "65",
    "member_id": "1",
    "order_no": "0931259760",
    "bonus_date": "2021-12-03 11:07:18",
    "bonus_type": "3",
    "bonus": "100"
  }
]
    *
    *
    *
    * */
    public static String bid;
    public String getbid() {
        return bid;
    }

    public void setbid(String bid) {
        this.bid = bid;
    }
    public static String bonus_date;
    public String getbonus_date() {
        return bonus_date;
    }

    public void setbonus_date(String bonus_date) {
        this.bonus_date = bonus_date;
    }
    public static String bonus_type;
    public String getbonus_type() {
        return bonus_type;
    }

    public void setbonus_type(String bonus_type) {
        this.bonus_type = bonus_type;
    }
    public  static String bonus;
    public String getbonus() {
        return bonus;
    }

    public void setbonus(String bonus) {
        this.bonus = bonus;
    }
}
