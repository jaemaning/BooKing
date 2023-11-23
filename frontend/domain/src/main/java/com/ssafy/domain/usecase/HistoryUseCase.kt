package com.ssafy.domain.usecase

import com.ssafy.domain.model.CreateSummaryRequest
import com.ssafy.domain.model.RecordFileNameRequest
import com.ssafy.domain.model.TranscriptionModificationRequest
import com.ssafy.domain.model.history.CreateSummaryResponse
import com.ssafy.domain.model.history.LoadSummaryResponse
import com.ssafy.domain.model.history.TranscriptionResponse
import com.ssafy.domain.repository.HistoryRepository
import retrofit2.Response
import javax.inject.Inject

class HistoryUseCase@Inject constructor(private val repository: HistoryRepository) {
    suspend fun postRecordFileName(request: RecordFileNameRequest) : Response<Unit> {
        return repository.postRecordFileName(request)
    }

    suspend fun getSpeakToText(meetinginfoId : Long) : TranscriptionResponse {
        return repository.getSpeakToText(meetinginfoId)
    }

    suspend fun createSummary(request: CreateSummaryRequest) : CreateSummaryResponse {
        return repository.createSummary(request)
    }

    suspend fun getSummary(transactionId : String) : LoadSummaryResponse {
        return repository.getSummary(transactionId)
    }

    suspend fun postTranscription(request: TranscriptionModificationRequest) : Response<Unit> {
        return repository.postTranscription(request)
    }
}