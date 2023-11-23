package com.ssafy.domain.model.mypage

data class UserModifyRequest (
    val loginId : String,
    val nickname: String,
    val profileImage : String,
)

data class AddressnModifyRequest (
//    val loginId :String,
    val address : String
)
