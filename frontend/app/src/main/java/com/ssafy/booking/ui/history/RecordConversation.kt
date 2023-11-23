package com.ssafy.booking.ui.history

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.ssafy.booking.R
import com.ssafy.booking.di.App
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.viewmodel.HistoryViewModel
import com.ssafy.booking.viewmodel.PlayerViewModel
import com.ssafy.domain.model.ChatRoom
import com.ssafy.domain.model.history.Segment
import com.ssafy.domain.model.history.TranscriptionResponse


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordDetail(
    meetinginfoId: String?
) {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val historyViewModel: HistoryViewModel = hiltViewModel()
    val navController = LocalNavigation.current

    if (meetinginfoId != null) {
        historyViewModel.loadTransaction(meetinginfoId.toLong())
    }

    val speakToTextInfo = historyViewModel.SpeakToTextInfo.observeAsState().value

    Log.d("STT_TEST", "녹음분석기록에서의 ${speakToTextInfo}")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .padding(12.dp)
    ) {
        STTList(historyViewModel, playerViewModel, speakToTextInfo)
    }
}

@Composable
fun STTList(
    historyViewModel: HistoryViewModel,
    playerViewModel: PlayerViewModel,
    speakToTextInfo: TranscriptionResponse?
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
            speakToTextInfo?.segments?.let { segments ->
                itemsIndexed(segments) { index, segment ->
                    SpeakToTextRow(historyViewModel, playerViewModel, segment)
                }
            }
        }
    }
}

@Composable
fun SpeakToTextRow(
    historyViewModel: HistoryViewModel,
    playerViewModel: PlayerViewModel,
    segment: Segment,
) {
    val context = LocalContext.current
    val imageLoader = LocalContext.current.imageLoader

    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            AsyncImage(
//                model = ImageRequest.Builder(context)
//                    .data("https://kr.object.ncloudstorage.com/booking-bucket/images/${segment.speaker.name}_profile.png")
//                    .memoryCachePolicy(CachePolicy.DISABLED)
//                    .addHeader("Host", "kr.object.ncloudstorage.com")
//                    .crossfade(true)
//                    .build(),
//                contentScale = ContentScale.Crop,
//                contentDescription = null,
//                imageLoader = imageLoader,
//                modifier = Modifier
//                    .size(32.dp)
//                    .clip(CircleShape),
//                error = painterResource(id = R.drawable.basic_profile)
//            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp) // 원의 크기 설정
                    .clip(CircleShape) // 원 모양으로 클립
                    .background(getColorForName(segment.speaker.name))
            ) {
                Text(
                    text = segment.speaker.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                )
            }
            Text(
                modifier = Modifier
                    .clickable(onClick = { playerViewModel.updateSliderPosition(segment.start.toInt()) }),
                text = playerViewModel.convertMillisToTimeFormat(segment.start.toInt()),
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Column {
            Spacer(modifier = Modifier.padding(4.dp))
            SelectionContainer(
                modifier = Modifier
                    .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Text(text = segment.text)
            }
        }
    }
    Spacer(modifier = Modifier.padding(4.dp))
}


@Composable
fun getColorForName(name: String): Color {
    return when (name.firstOrNull()?.uppercaseChar()) {
        'A' -> Color(0xFFFC9EBD)
        'B' -> Color(0xFFB8F3B8)
        'C' -> Color(0xFFFFDDA6)
        'D' -> Color(0xFFA8C8F9)
        'E' -> Color(0xFFCCD1FF)
        'F' -> Color(0xFFFFCCCC)
        'G' -> Color(0xFFD4F0F0)
        else -> Color.DarkGray
    }
}
