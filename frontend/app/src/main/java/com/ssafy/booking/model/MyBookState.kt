package com.ssafy.booking.model

import com.ssafy.domain.model.mybook.MyBookListResponse

sealed class MyBookState {

    object Loading : MyBookState()
    data class Success(val data: List<MyBookListResponse>) : MyBookState()
    data class Error(val message: String) : MyBookState()
}