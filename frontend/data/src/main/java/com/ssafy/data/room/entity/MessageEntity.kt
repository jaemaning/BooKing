package com.ssafy.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import java.sql.Timestamp
import java.time.LocalDateTime

@Entity(tableName = "messageEntity")
data class MessageEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "chatroom_id") val chatroomId : Int?,
    @ColumnInfo(name = "message_id") val messageId : Int?,
    @ColumnInfo(name = "sender_id") val senderId : Int?,
    @ColumnInfo(name = "content") val content : String?,
    @ColumnInfo(name = "read_count") val readCount : Int?,
    @ColumnInfo(name = "time_stamp") val timeStamp : String,

    @ColumnInfo(name = "used") val used: Boolean = false
)