package com.ssafy.domain.model

import com.google.gson.annotations.SerializedName
import com.ssafy.domain.model.history.Segment

data class RecordFileNameRequest (
    @SerializedName("fileName")
    val fileName : String,
    @SerializedName("meetingInfoId")
    val meetingInfoId : String,
)

data class CreateSummaryRequest(
    var content: String,
    val transcriptionId: String
)

data class TranscriptionModificationRequest(
    val id: String,
    val segments: List<Segment>,
    val text: String,
    val speakers: List<Speaker>,
    val createdAt: String,
    val fileName: String
)

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