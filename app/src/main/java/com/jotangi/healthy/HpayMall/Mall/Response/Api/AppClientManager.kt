package com.jotangi.healthy.HpayMall.Mall.Response.Api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppClientManager private constructor(){

    lateinit var service: ApiService

    private var logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Log.e("api","interceptor msg: $message")
        }
    }).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private var okHttpClient = OkHttpClient().newBuilder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(120, TimeUnit.SECONDS).addInterceptor(logging).build()

    fun init() {
        val retrofit2 = Retrofit.Builder()
            .baseUrl(Constants.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        service = retrofit2.create(ApiService::class.java)
    }

    companion object {
        val instance by lazy { AppClientManager() }
    }
}