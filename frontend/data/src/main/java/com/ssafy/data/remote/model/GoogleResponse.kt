package com.ssafy.data.remote.model

import com.google.gson.annotations.SerializedName
import java.util.jar.Attributes.Name

data class GoogleResponse (

    @SerializedName("name")
    val name: String?,
    @SerializedName("email")
    val email: String?
)