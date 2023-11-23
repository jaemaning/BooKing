package com.ssafy.domain.model

import com.google.gson.annotations.SerializedName

data class ChatCreateRequest (
    @SerializedName("meetingId")
    val meetingId : Int,
    @SerializedName("leaderId")
    val leaderId : Long?,
    @SerializedName("meetingTitle")
    val meetingTitle : String,
)

data class ChatJoinRequest (
    @SerializedName("meetingId")
    val meetingId : Int,
    @SerializedName("memberId")
    val memberId : Long?,
)

data class ChatExitRequest(
    @SerializedName("meetingId")
    val meetingId: String?,
    @SerializedName("memberId")
    val memberId: Long?,
)

data class LastReadMessageRequest(
    @SerializedName("lastMessageIndex")
    val lastMessageIndex: Int = 0
)

data class DisconnectSocket(
    @SerializedName("chatroomId")
    val chatRoomId: Int
)