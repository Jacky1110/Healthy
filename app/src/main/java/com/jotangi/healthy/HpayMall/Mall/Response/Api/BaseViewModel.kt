package com.jotangi.healthy.HpayMall.Mall.Response.Api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BaseViewModel(var ARepo: ApiRepository) : ViewModel() {
    //base response data
    val response = MediatorLiveData<String>()
    val code = MediatorLiveData<String>()
    val status = MediatorLiveData<String>()
    val login = MediatorLiveData<List<loginResponse>>()

    //login
    suspend fun getlogin(baseRequest: BaseRequest) {
        var data = ARepo.getlogin(baseRequest).data
        viewModelScope.launch(Dispatchers.IO) {
            if (data != null && data.isNotEmpty()) {
                val list = data
                if (list != null && list.isNotEmpty()) {
                   login.postValue(list!! as List<loginResponse>?)
                }
            }
        }

    }
}