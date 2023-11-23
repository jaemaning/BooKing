package com.ssafy.data.remote.api

import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.ChatExitRequest
import com.ssafy.domain.model.ChatJoinRequest
import com.ssafy.domain.model.ChatRoom
import com.ssafy.domain.model.LastReadMessageRequest
import com.ssafy.domain.model.MessageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApi {
    @GET("/api/chat/room/list")
    suspend fun getChatList() : List<ChatRoom>

    @Headers("Content-Type: application/json")
    @POST("/api/chat/room/")
    suspend fun postChatCreate(@Body request: ChatCreateRequest ): Response<Unit>

    @Headers("Content-Type: application/json")
    @POST("/api/chat/room/join")
    suspend fun postChatJoin(@Body request: ChatJoinRequest ): Response<Unit>

    @Headers("Content-Type: application/json")
    @POST("/api/chat/room/exit")
    suspend fun postChatExit(@Body request: ChatExitRequest): Response<Unit>

    @Headers("Content-Type: application/json")
    @POST("/api/chat/room/{chatroomId}")
    suspend fun postLastReadMessage(
        @Path("chatroomId") chatroomId: Int,
        @Body request: LastReadMessageRequest
    ): List<MessageResponse>

    @Headers("Content-Type: application/json")
    @DELETE("/api/chat/room/{chatroomId}")
    suspend fun deleteDisconnectSocket(
        @Path("chatroomId") chatroomId: Int,
    ): Response<Unit>
}