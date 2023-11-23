package com.ssafy.domain.repository

import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.ChatExitRequest
import com.ssafy.domain.model.ChatJoinRequest
import com.ssafy.domain.model.ChatRoom
import com.ssafy.domain.model.CreateSummaryRequest
import com.ssafy.domain.model.LastReadMessageRequest
import com.ssafy.domain.model.MessageResponse
import com.ssafy.domain.model.RecordFileNameRequest
import com.ssafy.domain.model.TranscriptionModificationRequest
import com.ssafy.domain.model.history.CreateSummaryResponse
import com.ssafy.domain.model.history.LoadSummaryResponse
import com.ssafy.domain.model.history.TranscriptionResponse
import retrofit2.Response
interface HistoryRepository {
    suspend fun postRecordFileName(request: RecordFileNameRequest) : Response<Unit>
    suspend fun getSpeakToText(meetinginfoId: Long) : TranscriptionResponse
    suspend fun createSummary(request: CreateSummaryRequest) : CreateSummaryResponse
    suspend fun getSummary(transactionId: String) : LoadSummaryResponse
    suspend fun postTranscription(request: TranscriptionModificationRequest) : Response<Unit>

}