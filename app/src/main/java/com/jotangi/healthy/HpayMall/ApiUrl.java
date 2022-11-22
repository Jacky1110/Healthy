package com.jotangi.healthy.HpayMall;

/**
 * @author Kelly
 * @version hpay_32版
 * @Title:
 * @Package com.jotangi.healthy.HpayMall
 * @Description:
 * @date 2022-02-07
 */
public class ApiUrl {
    // 正式機
//    public static final String API_URL = "https://medicalec.jotangi.net/";
    // 測試機
//    public static final String API_URL = "https://tripspottest.jotangi.net/";
    public static final String API_URL = "https://pay.digimed.tw/";
    public static final String payUrl = API_URL + "ecpay/ecpayindex.php?orderid=";
    //https://tripspottest.jotangi.net/ecpay/ecpayindex.php?orderid=
    //public static final String API_URL = "http://211.20.185.2/medicalec/api/";
    //https://tripspottest.jotangi.net/medicalec/api
    //api    constant
    public static String app_version = "medicalec/api/system_setting.php";
    public static String LOGIN = "medicalec/api/user_login.php";
    public static String user_register = "medicalec/api/user_register.php";
    public static String user_code = "medicalec/api/user_code.php";
    public static String user_logut = "medicalec/api/user_logut.php";
    public static String user_changepwd = "medicalec/api/user_changepwd.php";
    public static String user_edit = "medicalec/api/user_edit.php";
    public static String user_delete = "medicalec/api/user_unregister.php";
    public static String store_list = "medicalec/api/store_list.php";
    public static String member_info = "medicalec/api/member_info.php";
    public static String product_type = "medicalec/api/product_type.php";
    public static String product_list = "medicalec/api/product_list.php";
    public static String product_info = "medicalec/api/product_info.php";
    public static String user_resetpwd = "medicalec/api/user_resetpwd.php";
    public static String bonus_list = "medicalec/api/bonus_list.php";
    public static String order_list = "medicalec/api/order_list.php";
    public static String get_payment_url = "medicalec/api/get_payment_url.php";
    public static String coupon_list = "medicalec/api/coupon_list.php";
    //recommend_list
    public static String recommend_list = "medicalec/api/recommend_list.php";
    //user_uploadpic
    public static String user_uploadpic = "medicalec/api/user_uploadpic.php";
    //新增商品到購物車
    public static String add_shoppingcart = "medicalec/api/add_shoppingcart.php";
    //購物車商品列表
    public static String shoppingcart_list = "medicalec/api/shoppingcart_list.php";
    //edit_shoppingcart  修改購物車內商品數量
    public static String edit_shoppingcart = "medicalec/api/edit_shoppingcart.php";
    //del_shoppingcart 修改購物車內商品數量
    public static String del_shoppingcart = "medicalec/api/del_shoppingcart.php";
    //shoppingcart_count 購物車內商品總數
    public static String shoppingcart_count = "medicalec/api/shoppingcart_count.php";
    //ecorder_list.php 商城訂單列表
    public static String ecorder_list = "medicalec/api/ecorder_list.php";
    //ecorder_info商城訂單明細
    public static String ecorder_info = "medicalec/api/ecorder_info.php";
    //add_ecorder
    public static String add_ecorder = "medicalec/api/add_ecorder.php";
    //store_info掃描BARCODE先 call 這隻確認cnannel_price=0 (無折扣) || 1(有)
    public static String store_info = "medicalec/api/store_info.php";
    //clear_shoppingcart清空購物車
    public static String clear_shoppingcart = "medicalec/api/clear_shoppingcart.php";
}
