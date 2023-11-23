package com.ssafy.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssafy.data.room.entity.ChatEntity

@Dao
interface ChatDao {
    @Query("SELECT * FROM chatEntity")
    fun getAll() : List<ChatEntity>

    // 최초 저장
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertChatIdFirstTime(chatEntity: ChatEntity)
    
    // 마지막으로 읽은 메시지 불러오기
    @Query("SELECT lastReadMessage_idx FROM chatEntity WHERE chatroomId = :chatroomId")
    fun getLastReadMessageId(chatroomId: Int): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLastReadMessage(chatEntity: ChatEntity)


}