package com.ssafy.domain.model.kakaopay

import com.google.gson.annotations.SerializedName

data class KakaoPayRequest (

    @SerializedName("amount")
    val amount : String
)