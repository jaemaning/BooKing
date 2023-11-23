package com.ssafy.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ssafy.data.room.dao.ChatDao
import com.ssafy.data.room.entity.ChatEntity
import com.ssafy.data.room.Converters

@Database(entities = [ChatEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
}