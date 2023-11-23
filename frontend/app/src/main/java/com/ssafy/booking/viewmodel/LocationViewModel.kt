package com.ssafy.booking.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.loacation.AddressResponse
import com.ssafy.domain.model.loacation.KakaoSearchResponse
import com.ssafy.domain.usecase.LocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationUseCase: LocationUseCase
):ViewModel(){
    // GET - 위경도 주소 변환
    private val _getAddressResponse = MutableLiveData<Response<AddressResponse>>()
    val getAddressResponse: LiveData<Response<AddressResponse>> get() = _getAddressResponse

    // 서버 응답 상태 관리
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)
    fun getAddress(lgt: String, lat: String) = viewModelScope.launch {
        isLoading.value = true
        try {
            val response = locationUseCase.getAddress(lgt, lat)
            if (response.isSuccessful) {
                // 성공적인 응답 처리
                _getAddressResponse.value = response
                Log.d("위경도변환",response.body().toString())
            } else {
                // 에러 응답 처리
                errorMessage.value = response.errorBody()?.string() ?: "에러1"
            }
        } catch (e: Exception) {
            errorMessage.value = e.message ?: "에러2"
        } finally {
            // finally는 성공이든 실패든 무조건 실행
            isLoading.value = false
        }
    }

    private val _getKakaoSearchResponse = MutableLiveData<Response<KakaoSearchResponse>>()
    val getKakaoSearchResponse: LiveData<Response<KakaoSearchResponse>> get() = _getKakaoSearchResponse
    fun getSearchList(query:String,page:Int,size:Int,x:String,y:String,radius:Int) = viewModelScope.launch {
        isLoading.value = true
        try {
            val response = locationUseCase.getSearchList(query,page,size,x,y,radius)
            if (response.isSuccessful) {
                // 성공적인 응답 처리
                _getKakaoSearchResponse.value = response
            } else {
                // 에러 응답 처리
                errorMessage.value = response.errorBody()?.string() ?: "에러1"
            }
        } catch (e: Exception) {
            errorMessage.value = e.message ?: "에러2"
        } finally {
            // finally는 성공이든 실패든 무조건 실행
            isLoading.value = false
        }
    }

}