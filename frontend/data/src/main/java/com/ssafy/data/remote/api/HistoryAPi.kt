package com.ssafy.data.remote.api

import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.ChatJoinRequest
import com.ssafy.domain.model.CreateSummaryRequest
import com.ssafy.domain.model.RecordFileNameRequest
import com.ssafy.domain.model.TranscriptionModificationRequest
import com.ssafy.domain.model.history.CreateSummaryResponse
import com.ssafy.domain.model.history.LoadSummaryResponse
import com.ssafy.domain.model.history.TranscriptionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HistoryAPi {
    @Headers("Content-Type: application/json")
    @POST("/api/booking/stt")
    suspend fun postRecordFileName(@Body request: RecordFileNameRequest): Response<Unit>

    @GET("/api/booking/stt")
    suspend fun getSpeakToText(
        @Query("meetingInfoId") meetinginfoId: Long,
    ): TranscriptionResponse

    @Headers("Content-Type: application/json")
    @POST("/api/booking/summary")
    suspend fun createSummary(@Body request: CreateSummaryRequest): CreateSummaryResponse

    @GET("/api/booking/summary")
    suspend fun getSummary(
        @Query("transactionId") transactionId: String,
    ): LoadSummaryResponse

    @Headers("Content-Type: application/json")
    @POST("/api/booking/stt/modification")
    suspend fun postTranscription(@Body request: TranscriptionModificationRequest): Response<Unit>
}