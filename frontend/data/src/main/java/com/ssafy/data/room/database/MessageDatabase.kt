package com.ssafy.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ssafy.data.room.dao.MessageDao
import com.ssafy.data.room.Converters
import com.ssafy.data.room.entity.MessageEntity

@Database(entities = [MessageEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class MessageDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}