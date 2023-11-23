package com.ssafy.domain.model

import com.google.gson.annotations.SerializedName

data class DeviceToken(
    @SerializedName("deviceToken")
    val deviceToken: String?,
)