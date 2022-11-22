package com.jotangi.healthy.HpayMall.Mall.Response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *
 * @Title:
 * @Package com.jotangi.healthy.HpayMall
 * @Description:
 * @author Kelly
 * @date 2022-02-07
 * @version hpay_32版
 */
@Parcelize
data class MallOrder(
    var moId: String? = null,
    var moDate: String? = null,
    var moCount: String? = null,
    var moUnitPrice: String? = null,
    var moDollar: String? = null,
    var moStatus: String? = null,
    var IsPay: Boolean = false,
    var pay: String? = null,
    ) : Parcelable