package com.ssafy.data.repository

import com.ssafy.data.remote.api.ChatApi
import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.ChatExitRequest
import com.ssafy.domain.model.ChatJoinRequest
import com.ssafy.domain.model.ChatRoom
import com.ssafy.domain.model.LastReadMessageRequest
import com.ssafy.domain.model.MessageResponse
import com.ssafy.domain.repository.ChatRepository
import retrofit2.Response
import javax.inject.Inject

// 인터페이스 구현체
class ChatRepositoryImpl @Inject constructor(private val chatApi: ChatApi) : ChatRepository {
    override suspend fun getChatList(): List<ChatRoom> {
        return chatApi.getChatList()
    }

    override suspend fun postChatCreate(request: ChatCreateRequest): Response<Unit> {
        return chatApi.postChatCreate(request)
    }

    override suspend fun postChatJoin(request: ChatJoinRequest): Response<Unit> {
        return chatApi.postChatJoin(request)
    }

    override suspend fun postChatExit(request: ChatExitRequest): Response<Unit> {
        return chatApi.postChatExit(request)
    }

    override suspend fun postLastReadMessage(
        chatroomId: Int,
        request: LastReadMessageRequest
    ): List<MessageResponse> {
        return chatApi.postLastReadMessage(chatroomId, request)
    }

    override suspend fun deleteDisconnectSocket(
        chatroomId: Int,
    ): Response<Unit> {
        return chatApi.deleteDisconnectSocket(chatroomId)
    }
}

