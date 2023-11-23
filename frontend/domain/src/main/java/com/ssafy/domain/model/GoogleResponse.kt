package com.ssafy.domain.model

import com.google.gson.annotations.SerializedName

data class GoogleResponse (

    @SerializedName("name")
    val name: String?,
    @SerializedName("email")
    val email: String?
)