package com.ssafy.domain.model.booking

import com.google.gson.annotations.SerializedName

data class BookingBoardCreateRequest (

    @SerializedName("meetingId")
    val meetingId : Long,
    @SerializedName("title")
    val title : String,
    @SerializedName("content")
    val content : String
)

data class BookingBoardUpdateRequest (

    @SerializedName("postId")
    val postId : Long,
    @SerializedName("meetingId")
    val meetingId : Long,
    @SerializedName("title")
    val title : String,
    @SerializedName("content")
    val content : String
)