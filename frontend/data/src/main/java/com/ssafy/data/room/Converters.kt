package com.ssafy.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class Converters {
    @TypeConverter
    fun ListToJson(value: List<Int>): String {
        return Gson().toJson(value)
    }
    @TypeConverter
    fun jsonToList(value: String): List<Int> {
        return Gson().fromJson(value, Array<Int>::class.java).toList()
    }
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC) }
    }
    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    }
}