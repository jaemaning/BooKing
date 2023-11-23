package com.ssafy.booking.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.usecase.NaverCloudUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

// 플레이어 상태 관리
enum class PlayerState {
    STARTED, PAUSED
}

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val naverCloudUseCase: NaverCloudUseCase
) : ViewModel() {
    private val mediaPlayer = MediaPlayer()
    private val _playingState = MutableLiveData<PlayerState>(PlayerState.PAUSED)
    val playingState: LiveData<PlayerState> = _playingState

    private val _sliderPosition = MutableLiveData(0)
    val sliderPosition: LiveData<Int> = _sliderPosition
    private val _totalDuration = MutableLiveData(0)
    val totalDuration: LiveData<Int> = _totalDuration

    private val _naverCloudGetResponse = MutableLiveData<Response<ResponseBody>>()
    val naverCloudGetResponse : LiveData<Response<ResponseBody>> get() = _naverCloudGetResponse
    fun GetToNaverCloud(meetingInfoId: String?) =
        viewModelScope.launch {
            _naverCloudGetResponse.value = naverCloudUseCase.getObject("booking-bucket", "recording/${meetingInfoId}_recording.m4a")
        }

    fun setAudioFile(context: Context, audioFile: Uri, headers: MutableMap<String, String>) {
        mediaPlayer.apply {
            reset() // 현재 재생 중인 오디오가 있으면 리셋
            setDataSource(context, audioFile, headers) // 새 오디오 파일 설정
            prepare() // 오디오 준비
        }
    }

    init {
        // MediaPlayer 설정
        mediaPlayer.setOnPreparedListener {
            setTotalDuration(mediaPlayer)
        }
        mediaPlayer.setOnCompletionListener {
            _playingState.value = PlayerState.PAUSED
            _sliderPosition.value = 0
        }
    }



    fun playAudio() {
        if(_playingState.value == PlayerState.PAUSED) {
                mediaPlayer.start() // 재생 시작
                startUpdatingPlaybackPosition()
                _playingState.value = PlayerState.STARTED
        } else if(_playingState.value == PlayerState.STARTED) {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause() // 재생 중지
                _playingState.value = PlayerState.PAUSED
            }
        }
    }

    private fun startUpdatingPlaybackPosition() {
        val handler = android.os.Handler(Looper.getMainLooper())
        val updateTask = object : Runnable {
            override fun run() {
                try {
                    _sliderPosition.value = mediaPlayer.currentPosition
                    handler.postDelayed(this, 1000)
                } catch(e : Exception) {
                    Log.d("Player", "$e")
                }
            }
        }
        handler.postDelayed(updateTask, 1000)
    }

    fun updateSliderPosition(newPosition: Int) {
        _sliderPosition.value = newPosition
        mediaPlayer.seekTo(newPosition)
    }

    fun convertMillisToTimeFormat(millis: Int): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun setTotalDuration(mediaPlayer: MediaPlayer) {
        val durationMillis = mediaPlayer.duration
        _totalDuration.value = durationMillis
    }


    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        _sliderPosition.value = 0
        _totalDuration.value = 0
    }
}