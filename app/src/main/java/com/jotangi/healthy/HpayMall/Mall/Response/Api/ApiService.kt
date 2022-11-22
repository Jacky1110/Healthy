package com.jotangi.healthy.HpayMall.Mall.Response.Api

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface  ApiService {
    @FormUrlEncoded
    @POST(Constants.user_login)
    suspend fun user_login(
        @Field("member_id")account : String,
        @Field("member_pwd")pwd : String) : loginResponse

}