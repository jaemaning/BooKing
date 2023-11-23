package com.ssafy.domain.repository

import com.ssafy.domain.model.kakaopay.KakaoPayRequest
import com.ssafy.domain.model.kakaopay.KakaoPayResponse
import retrofit2.Response

interface KakaoPayRepository {

    suspend fun KakaoPay(request: KakaoPayRequest) : Response<KakaoPayResponse>
}