package com.ssafy.booking.ui.history

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.booking.viewmodel.PlayerViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.ssafy.booking.R
import com.ssafy.booking.di.App
import com.ssafy.booking.utils.ObjectStorageInterceptor
import com.ssafy.booking.viewmodel.PlayerState
import okhttp3.Interceptor


@Composable
fun PlayerController(
    meetingInfoId: String?,
) {
    val context = LocalContext.current
    val playerViewModel : PlayerViewModel = hiltViewModel()
    val playerState by playerViewModel.playingState.observeAsState()

    val accessKey = "64tVP74TUGmd6PDzjQ04"
    val secretKey = "1E11TfvJcy7OVnSSm3rV0Vph24CLUO4Tiehd5PtZ"
    val region = "kr-standard"

    val headers = mutableMapOf<String, String>()

    headers["Host"] = "kr.object.ncloudstorage.com"
    App.prefs.getNcpAuthHeader()?.let { headers["Authorization"] = it }
    App.prefs.getNcpTimeStamp()?.let { headers["x-amz-date"] = it }
    App.prefs.getNcpPayloadHash()?.let { headers["x-amz-content-sha256"] = it }

    val resourceUri = Uri.parse("https://kr.object.ncloudstorage.com/booking-bucket/recording/${meetingInfoId}_recording.m4a")
    LaunchedEffect(Unit) {
        playerViewModel.setAudioFile(context, resourceUri, headers)
    }

    Box(
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(12.dp)
        ) {
            IconButton(
                onClick = {
                    playerViewModel.playAudio()
                },
            ) {
                if (playerState == PlayerState.STARTED) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_pause_circle_24),
                        contentDescription = "정지",
                        modifier = Modifier.size(100.dp),
                        tint = Color(0xFF00C68E)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                        contentDescription = "재생",
                        modifier = Modifier.size(100.dp),
                        tint = Color(0xFF00C68E)
                    )
                }
            }
            SeekBar(playerViewModel)
        }
    }
}

@Composable
fun SeekBar(
    playerViewModel: PlayerViewModel
) {
    val sliderPosition by playerViewModel.sliderPosition.observeAsState(0)
    val totalDuration by playerViewModel.totalDuration.observeAsState(0)


    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Slider(
            value = sliderPosition.toFloat(),
            onValueChange = { newPosition ->
                playerViewModel.updateSliderPosition(newPosition.toInt())
            },
            valueRange = 0f..totalDuration.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF00C68E),
                activeTrackColor = Color(0xFF00C68E),
                inactiveTrackColor = Color(0xFFDAF6EE),
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        Text(
            text = "${playerViewModel.convertMillisToTimeFormat(sliderPosition)} / ${playerViewModel.convertMillisToTimeFormat(totalDuration)}"
        )
    }
}



