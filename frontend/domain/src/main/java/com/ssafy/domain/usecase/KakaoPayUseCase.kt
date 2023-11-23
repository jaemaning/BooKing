package com.ssafy.domain.usecase

import com.ssafy.domain.model.kakaopay.KakaoPayRequest
import com.ssafy.domain.model.kakaopay.KakaoPayResponse
import com.ssafy.domain.repository.KakaoPayRepository
import retrofit2.Response
import javax.inject.Inject

class KakaoPayUseCase @Inject constructor(
    private val repository : KakaoPayRepository
) {

    suspend fun KakaoPay(request: KakaoPayRequest) : Response<KakaoPayResponse> {
        return repository.KakaoPay(request)
    }
}