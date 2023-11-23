package com.ssafy.domain.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ChatRoom (
    @SerializedName("chatroomId")
    val chatroomId: Int,
    @SerializedName("lastMessageIdx")
    val lastMessageIdx: Int,
    @SerializedName("meetingTitle")
    val meetingTitle : String,
    @SerializedName("lastMessage")
    val lastMessage : String?,
    @SerializedName("memberList")
    val memberList: List<Int>,
    @SerializedName("coverImage")
    val coverImage: String
)

data class MessageResponse (
    @SerializedName("chatroomId")
    val chatroomId: Int,
    @SerializedName("messageId")
    val messageId: Int,
    @SerializedName("senderId")
    val senderId: Int,
    @SerializedName("content")
    val content: String,
    @SerializedName("readCount")
    val readCount: Int,
    @SerializedName("timestamp")
    val timestamp: String
)