package com.ssafy.domain.model
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class KafkaMessage(
    @SerializedName("message")
    val message: String,
    @SerializedName("senderId")
    val senderId: Long?,
    @SerializedName("sendTime")
    val sendTime: LocalDateTime,
    @SerializedName("senderName")
    val senderName: String,
)