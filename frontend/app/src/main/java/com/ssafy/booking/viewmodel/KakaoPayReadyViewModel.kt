package com.ssafy.booking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.kakaopay.KakaoPayRequest
import com.ssafy.domain.model.kakaopay.KakaoPayResponse
import com.ssafy.domain.model.mybook.MyBookListResponse
import com.ssafy.domain.usecase.KakaoPayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class KakaoPayReadyViewModel @Inject constructor(
    private val kakaoPayUseCase: KakaoPayUseCase
) :ViewModel() {

    private val _kakaoPayResponse = MutableLiveData<Response<KakaoPayResponse>>()
    val kakaoPayResponse : LiveData<Response<KakaoPayResponse>> get() = _kakaoPayResponse

    fun getKakaoPayPage(request: KakaoPayRequest) =
        viewModelScope.launch {
            _kakaoPayResponse.value = kakaoPayUseCase.KakaoPay(request)
        }
}