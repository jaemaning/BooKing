package com.ssafy.booking.di

import android.content.Context
import androidx.room.Room
import com.ssafy.data.room.dao.ChatDao
import com.ssafy.data.room.dao.MessageDao
import com.ssafy.data.room.database.ChatDatabase
import com.ssafy.data.room.database.MessageDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideMessageDatabase(@ApplicationContext appContext: Context): MessageDatabase {
        return Room.databaseBuilder(
            appContext,
            MessageDatabase::class.java,
            "message_database",
        ).build()
    }

    @Provides
    @Singleton
    fun provideChatDatabase(@ApplicationContext appContext: Context): ChatDatabase {
        return Room.databaseBuilder(
            appContext,
            ChatDatabase::class.java,
            "chat_database",
        ).build()
    }

    @Provides
    fun provideMessageDao(database: MessageDatabase): MessageDao {
        return database.messageDao()
    }

    @Provides
    fun provideChatDao(database: ChatDatabase): ChatDao {
        return database.chatDao()
    }
}
