package com.ssafy.data.repository

import android.content.om.OverlayManagerTransaction
import com.ssafy.data.remote.api.HistoryAPi
import com.ssafy.domain.model.CreateSummaryRequest
import com.ssafy.domain.model.RecordFileNameRequest
import com.ssafy.domain.model.TranscriptionModificationRequest
import com.ssafy.domain.model.history.CreateSummaryResponse
import com.ssafy.domain.model.history.LoadSummaryResponse
import com.ssafy.domain.model.history.TranscriptionResponse
import com.ssafy.domain.repository.HistoryRepository
import retrofit2.Response
import javax.inject.Inject

class HistoryRepositoryImpl  @Inject constructor(private val historyAPi: HistoryAPi) : HistoryRepository {
    override suspend fun postRecordFileName(request: RecordFileNameRequest): Response<Unit> {
        return historyAPi.postRecordFileName(request)
    }

    override suspend fun getSpeakToText(meetinginfoId : Long) : TranscriptionResponse {
        return historyAPi.getSpeakToText(meetinginfoId)
    }

    override suspend fun createSummary(request: CreateSummaryRequest): CreateSummaryResponse {
        return historyAPi.createSummary(request)
    }

    override suspend fun getSummary(transactionId: String): LoadSummaryResponse {
        return historyAPi.getSummary(transactionId)
    }

    override suspend fun postTranscription(request: TranscriptionModificationRequest): Response<Unit> {
        return historyAPi.postTranscription(request)
    }
}


