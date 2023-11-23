package com.ssafy.booking.ui.history

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.imageLoader
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.viewmodel.HistoryViewModel
import com.ssafy.booking.viewmodel.PlayerViewModel
import com.ssafy.domain.model.TranscriptionModificationRequest
import com.ssafy.domain.model.history.Segment
import com.ssafy.domain.model.Speaker


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRecordDetail(
    meetinginfoId: String?
) {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val historyViewModel: HistoryViewModel = hiltViewModel()
    val navController = LocalNavigation.current

    if (meetinginfoId != null) {
        historyViewModel.loadTransaction(meetinginfoId.toLong())
    }

    val speakToTextInfo = historyViewModel.SpeakToTextInfo.observeAsState().value
    val editedSegments = remember { mutableStateOf(speakToTextInfo?.segments ?: listOf()) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .padding(12.dp)
                .weight(1f) // 나머지 공간을 모두 차지
        ) {
            NewSTTList(historyViewModel, playerViewModel, editedSegments)
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF00C68E))
                .clickable {
                    val convertedSpeakers = speakToTextInfo?.speakers?.map { convertSpeaker(it) } ?: listOf()
                    val request = TranscriptionModificationRequest(
                        id = speakToTextInfo?.id ?: "",
                        segments = editedSegments.value,
                        text = speakToTextInfo?.text ?: "",
                        speakers = convertedSpeakers,
                        createdAt = speakToTextInfo?.createdAt ?: "",
                        fileName = speakToTextInfo?.fileName ?: ""
                    )
                    Log.d("뉴리코드업데이트", "${request}")
                    historyViewModel.postTranscription(request)
                }
        ) {
            Text(
                text = "수정하기",
                modifier = Modifier.padding(16.dp),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun convertSpeaker(historySpeaker: com.ssafy.domain.model.history.Speaker): com.ssafy.domain.model.Speaker {
    return com.ssafy.domain.model.Speaker(
        label = historySpeaker.label,
        name = historySpeaker.name
        // 다른 필드가 있다면 여기에 추가
    )
}

@Composable
fun NewSTTList(
    historyViewModel: HistoryViewModel,
    playerViewModel: PlayerViewModel,
    editedSegments: MutableState<List<Segment>>
) {
    val context = LocalContext.current
    val navController = LocalNavigation.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .padding(8.dp)
    ) {
        LazyColumn {
            itemsIndexed(editedSegments.value) { index, segment ->
                NewSpeakToTextRow(segment) { newSpeakerName, newText ->
                    val updatedSegments = editedSegments.value.toMutableList().apply {
                        this[index] = this[index].copy(
                            speaker = this[index].speaker.copy(name = newSpeakerName),
                            text = newText
                        )
                    }
                    editedSegments.value = updatedSegments
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewSpeakToTextRow(
    segment: Segment,
    onTextUpdate: (String, String) -> Unit
) {
    val context = LocalContext.current
    val imageLoader = LocalContext.current.imageLoader
    val playerViewModel: PlayerViewModel = hiltViewModel()

    var speakerName by rememberSaveable { mutableStateOf(segment.speaker.name) }
    var segmentText by rememberSaveable { mutableStateOf(segment.text) }

    Row {
        Column {
            Text(
                modifier = Modifier
                    .clickable(onClick = { playerViewModel.updateSliderPosition(segment.start.toInt()) }),
                text = playerViewModel.convertMillisToTimeFormat(segment.start.toInt()),
                fontSize = 14.sp,
                color = Color.Gray
            )
            OutlinedTextField(
                value = speakerName,
                onValueChange = { newName ->
                    speakerName = newName
                    onTextUpdate(newName, segmentText)
                },
                maxLines = 1,
                textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.width(50.dp)
            )
        }
        OutlinedTextField(
            value = segmentText,
            onValueChange = { newText ->
                segmentText = newText
                onTextUpdate(speakerName, newText)
            },
            label = { Text("내용") },
            modifier = Modifier
                .fillMaxWidth(),
            maxLines = Int.MAX_VALUE,
            textStyle = TextStyle(fontSize = 14.sp)
        )
    }
}
