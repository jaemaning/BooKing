package com.ssafy.domain.model.history

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

//data class SttResponseDto(
//    val result: String,
//    val message: String,
//    val token: String,
//    val version: String,
//    val params: Params,
//    val progress: Int,
//    val keywords: Map<String, List<String>>,
//    val segments: List<Segment>,
//    val text: String,
//    val confidence: Double,
//    val speakers: List<Speaker>
//)
//
//data class Params(
//    val service: String,
//    val domain: String,
//    val lang: String,
//    val completion: String,
//    val diarization: Diarization,
//    val boostings: List<String>,
//    val forbiddens: String,
//    val wordAlignment: Boolean,
//    val fullText: Boolean,
//    val noiseFiltering: Boolean,
//    val resultToObs: Boolean,
//    val priority: Int,
//    val userdata: UserData
//)
//
//data class Diarization(
//    val enable: Boolean,
//    val speakerCountMin: Int,
//    val speakerCountMax: Int
//)
//
//data class UserData(
//    @SerializedName("_ncp_DomainCode")
//    val ncpDomainCode: String,
//
//    @SerializedName("_ncp_DomainId")
//    val ncpDomainId: Int,
//
//    @SerializedName("_ncp_TaskId")
//    val ncpTaskId: Int,
//
//    @SerializedName("_ncp_TraceId")
//    val ncpTraceId: String
//)
//
data class Segment(
    val start: Long,
    val end: Long,
    val text: String,
    val textEdited: String,
    val speaker: Speaker
)
data class Speaker(
    val label: String,
    val name: String
)

data class TranscriptionResponse (
    val id: String,
    val segments: List<Segment>,
    val text: String,
    val speakers: List<Speaker>,
    val createdAt: String,
    val fileName: String
)

data class CreateSummaryResponse(
    val summary: String
)

data class LoadSummaryResponse(
    val id: String,
    val text: String,
    val transcriptionId: String,
    val createdAt: String
)