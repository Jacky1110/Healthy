package com.jotangi.healthy.HpayMall.Mall.Response.Api

class ApiRepository {
    suspend fun getlogin(baseRequest: BaseRequest): loginResponse {
        return try {
            AppClientManager.instance.service.user_login(
              baseRequest.member_id,
                baseRequest.member_pwd
            )
        } catch (e: Exception) {
            loginResponse()
        }
    }
}