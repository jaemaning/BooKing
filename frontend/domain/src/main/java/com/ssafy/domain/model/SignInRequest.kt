package com.ssafy.domain.model

data class SignInRequest(
    val loginId: String?,
    val email: String?,
    val age: Int?,
    val gender: String?,
    val nickname: String?,
    val fullName: String?,
    val address: String?,
    val profileImage: String?,
    val provider: String?
)