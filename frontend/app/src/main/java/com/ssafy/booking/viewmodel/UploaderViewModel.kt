package com.ssafy.booking.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.booking.model.UserInfoChangeResult
import com.ssafy.domain.model.RecordFileNameRequest
import com.ssafy.domain.model.mypage.UserModifyRequest
import com.ssafy.domain.usecase.HistoryUseCase
import com.ssafy.domain.usecase.NaverCloudUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UploaderViewModel @Inject constructor(
    private val naverCloudUseCase: NaverCloudUseCase,
    private val historyUseCase: HistoryUseCase
) : ViewModel() {
    // NAVER CLOUD GET
    private val _naverCloudGetResponse = MutableLiveData<Response<ResponseBody>>()
    val naverCloudGetResponse : LiveData<Response<ResponseBody>> get() = _naverCloudGetResponse

    private val _uploadStatus = MutableStateFlow(false)
    val uploadStatus: StateFlow<Boolean> = _uploadStatus.asStateFlow()

    // 파일 요청
    fun GetToNaverCloud(meetingInfoId: String?) =
        viewModelScope.launch {
            _naverCloudGetResponse.value = naverCloudUseCase.getObject("booking-bucket", "recording/${meetingInfoId}_recording.m4a")
            val response = naverCloudUseCase.getObject("booking-bucket", "recording/${meetingInfoId}_recording.m4a")
        }

    fun enrollRecordFile(meetingInfoId: String, requestBody: RequestBody?) {
        val requestInfo = RecordFileNameRequest(fileName = "${meetingInfoId}_recording.m4a", meetingInfoId = meetingInfoId)
        viewModelScope.launch {
            _uploadStatus.value = true
            try {
                if (requestBody != null) {
                    // POST CLOUD
                    naverCloudUseCase.putObject(
                        "booking-bucket",
                        "recording/${meetingInfoId}_recording.m4a",
                        requestBody
                    )
                    // POST SERVER
                    val response = historyUseCase.postRecordFileName(requestInfo)
                    if (response.isSuccessful) {
                        Log.d("HISTORY_TEST", "PostRecordFileName ${response}")
                    } else {
                        Log.d("HISTORY_TEST", "PostRecordFileName ${response}")
                    }
                    _uploadStatus.value = false
                }
            } catch (e:Exception) {
                _uploadStatus.value = false
            }
        }
    }

}