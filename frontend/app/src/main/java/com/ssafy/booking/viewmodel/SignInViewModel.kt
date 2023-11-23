package com.ssafy.booking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.SignInRequest
import com.ssafy.domain.usecase.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _signInResponse = MutableLiveData<Response<String>>()
    val signInResponse: LiveData<Response<String>> get() = _signInResponse

    fun signIn(request: SignInRequest) = viewModelScope.launch {
        _signInResponse.value = signInUseCase.execute(request)
    }
}
