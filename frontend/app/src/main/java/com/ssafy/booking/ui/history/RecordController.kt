package com.ssafy.booking.ui.history

import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import android.Manifest
import android.graphics.drawable.PaintDrawable
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.booking.viewmodel.PlayerViewModel
import com.ssafy.booking.viewmodel.RecordingState
import com.ssafy.booking.R
import com.ssafy.booking.viewmodel.RecorderViewModel
import dagger.hilt.android.lifecycle.HiltViewModel


@Composable
fun RecordController(
) {
    val context = LocalContext.current
    val recorderViewModel: RecorderViewModel = hiltViewModel()
    val recordingState by recorderViewModel.recordingState.observeAsState(RecordingState.STOPPED)

    val recordingDuration by recorderViewModel.recordingDuration.observeAsState(0)

    // 권한 요청
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(context, "녹음을 시작합니다.", Toast.LENGTH_SHORT).show()
                recorderViewModel.startRecording()
            } else {
                Toast.makeText(context, "녹음 기능을 사용하려면 권한을 수락해야 합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // 녹음 저장 버튼 - 오른쪽 상단 정렬
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
        ) {
            IconButton(
                onClick = {
                    if (recordingState == RecordingState.PAUSED) {
                        recorderViewModel.stopRecording()
                        Toast.makeText(context, "녹음을 저장합니다.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_save_24),
                    contentDescription = "녹음 저장",
                    tint = if (recordingState == RecordingState.PAUSED) Color(0xFF101828) else Color.LightGray
                )
            }
            Text(text = "저장하기", fontSize=12.sp)
        }

        // 나머지 컨트롤 - 중앙 정렬
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center, // 요소들을 상단과 하단에 분산 배치
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.padding(20.dp))
            AmplitudeIndicator()
            Spacer(modifier = Modifier.padding(20.dp))
            // 시작
            if (recordingState == RecordingState.STOPPED) {
                IconButton(
                    onClick = {
                        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    },
                    modifier = Modifier
                        .size(100.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                        contentDescription = "녹음 시작",
                        tint = Color(0xFF00C68E)
                    )
                }
            } else if (recordingState == RecordingState.STARTED || recordingState == RecordingState.RESUMED) {
                IconButton(
                    onClick = {
                        recorderViewModel.pauseRecording()
                        Toast.makeText(context, "녹음을 정지합니다.", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .size(100.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_pause_circle_24),
                        contentDescription = "녹음 중지",
                        tint = Color(0xFFFF1658)
                    )
                }
            } else if (recordingState == RecordingState.PAUSED) {
                IconButton(
                    onClick = {
                        Log.d("RECORD_STATE", "$recordingState")
                        recorderViewModel.resumeRecording()
                        Toast.makeText(context, "녹음을 재개합니다.", Toast.LENGTH_SHORT).show()
                        Log.d("RECORD_STATE", "$recordingState")
                    },
                    modifier = Modifier
                        .size(100.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                        contentDescription = "녹음 재개",
                        tint = Color(0xFF00C68E)
                    )
                }
            }
            Text(text="${recorderViewModel.convertMillisToTimeFormat(recordingDuration.toInt())}")
        }
    }
}

@Composable
fun AmplitudeIndicator(

) {
    val recorderViewModel: RecorderViewModel = hiltViewModel()
    val amplitude by recorderViewModel.amplitude.observeAsState(0)
    val amplitudes = remember { mutableStateListOf<Int>() }

    LaunchedEffect(amplitude) {
        amplitudes.add(0, amplitude)
        if (amplitudes.size > MAX_AMPLITUDE_SIZE) {
            amplitudes.removeLast()
        }
    }

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)) {
        val centerY = size.height / 2
        val maxAmplitudeHeight = size.height * 1f  // 그래프의 최대 높이
        var offsetX = size.width

        amplitudes.forEach { currentAmplitude ->
            val lineLength = (currentAmplitude / MAX_AMPLITUDE).toFloat() * maxAmplitudeHeight
            offsetX -= LINE_SPACE
            if (offsetX < 0) return@forEach

            drawLine(
                color = Color(0xFF00C68E),
                start = Offset(offsetX, centerY - lineLength / 2),
                end = Offset(offsetX, centerY + lineLength / 2),
                strokeWidth = LINE_WIDTH
            )
        }
    }
}

private const val MAX_AMPLITUDE = 32767f
private const val MAX_AMPLITUDE_SIZE = 100  // 표시할 최대 진폭 수
private const val LINE_WIDTH = 5f
private const val LINE_SPACE = 10f