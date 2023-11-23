package com.ssafy.data.remote.api

import com.ssafy.domain.model.kakaopay.KakaoPayRequest
import com.ssafy.domain.model.kakaopay.KakaoPayResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface KakaoPayApi {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/api/payments/ready")
    suspend fun KakaoPay(@Body request: KakaoPayRequest) : Response<KakaoPayResponse>
}