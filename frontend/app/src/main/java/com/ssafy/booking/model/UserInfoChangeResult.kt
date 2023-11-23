package com.ssafy.booking.model

sealed class UserInfoChangeResult {
    data class Success(val nick: String, val pImg: String, val destination: String) : UserInfoChangeResult()
    data class Error(val isErrorNick: Boolean) : UserInfoChangeResult()
}
