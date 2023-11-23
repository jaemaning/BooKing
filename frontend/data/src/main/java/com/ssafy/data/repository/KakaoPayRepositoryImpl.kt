package com.ssafy.data.repository

import com.ssafy.data.remote.api.KakaoPayApi
import com.ssafy.domain.model.kakaopay.KakaoPayRequest
import com.ssafy.domain.model.kakaopay.KakaoPayResponse
import com.ssafy.domain.repository.KakaoPayRepository
import retrofit2.Response
import javax.inject.Inject

class KakaoPayRepositoryImpl @Inject constructor(
    private val kakaoPayApi: KakaoPayApi
) : KakaoPayRepository {
    override suspend fun KakaoPay(request: KakaoPayRequest): Response<KakaoPayResponse> {
        return kakaoPayApi.KakaoPay(request)
    }
}