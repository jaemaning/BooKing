package com.ssafy.booking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.data.repository.token.TokenDataSource
import com.ssafy.domain.usecase.MyPageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val prefs : TokenDataSource,
    private val myPageUseCase: MyPageUseCase
) : ViewModel() {

    fun getLoginId() : String? {
        return prefs.getLoginId()
    }

    fun logout() {
        prefs.removeLoginId()
        prefs.removeToken()
        prefs.removeNickName()
        prefs.removeProfileImage()
    }

    fun postUserDelete(loginId : String) =
        viewModelScope.launch {
            myPageUseCase.deleteUser(loginId)
            logout()
        }

}