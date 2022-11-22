package com.jotangi.healthy.HpayMall.Mall.Response.Api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class BaseResponse (
    var status: String? = null,
    var code: String? = null,
    var responseMessage: String? = null
): Parcelable
@Parcelize
data class loginResponse(
    var data: List<BaseResponse>?= listOf()
):Parcelable,BaseResponse()
/*
* */
@Parcelize
data class MemberInfoRes (
    var mid: String? = null,
    var member_id: String? = null,
    var member_pwd: String? = null,
    var member_name: String? = null,
    var member_type: String? = null,
    var member_gender: String? = null,
    var member_email: String? = null,
    var member_birthday: String? = null,
    var member_address: String? = null,
    var member_phone: String? = null,
    var member_picture: String? = null,
    var member_totalpoints: String? = null,
    var member_usingpoints: String? = null,
    var member_status: String? = null,
    var recommend_code: String? = null,

    ): Parcelable