package com.ssafy.domain.model.booking

import com.google.gson.annotations.SerializedName

data class BookingBoardCreateResponse (

    @SerializedName("postId")
    val postId : Long
)

data class BookingBoardReadListResponse (

    @SerializedName("postId")
    val postId : Long,
    @SerializedName("meetingId")
    val meetingId : Long,
    @SerializedName("memberId")
    val memberId : Int,
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("profileImage")
    val profileImage : String,
    @SerializedName("title")
    val title : String,
    @SerializedName("createdAt")
    val createdAt : String,
    @SerializedName("updatedAt")
    val updatedAt : String,
)

data class BookingBoardReadDetailResponse (

    @SerializedName("postId")
    val postId : Long,
    @SerializedName("meetingId")
    val meetingId : Long,
    @SerializedName("memberId")
    val memberId : Int,
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("profileImage")
    val profileImage : String,
    @SerializedName("title")
    val title : String,
    @SerializedName("content")
    val content : String,
    @SerializedName("createdAt")
    val createdAt : String,
    @SerializedName("updatedAt")
    val updatedAt : String
)

data class BookingBoardUpdateResponse (

    @SerializedName("postId")
    val postId : Long,
    @SerializedName("meetingId")
    val meetingId : Long,
    @SerializedName("memberId")
    val memberId : Int,
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("profileImage")
    val profileImage : String,
    @SerializedName("title")
    val title : String,
    @SerializedName("content")
    val content : String,
    @SerializedName("createdAt")
    val createdAt : String,
    @SerializedName("updatedAt")
    val updatedAt : String
)