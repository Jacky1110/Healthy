package com.jotangi.healthy.HpayMall.Mall.Response.Api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BaseRequest (
    var member_id: String = "",
    var member_pwd: String = "",
): Parcelable
@Parcelize
data class MemberInfo (
    var member_id: String = "",
    var member_pwd: String = "",
): Parcelable
@Parcelize
data class MemberRegister(
    var member_id: String = "",
    var member_pwd: String = "",
    var member_name: String = "",
    var member_type: String = "",
    var recommend_code	: String ?= null,
    ): Parcelable