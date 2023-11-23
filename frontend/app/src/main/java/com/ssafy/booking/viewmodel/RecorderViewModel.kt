package com.ssafy.booking.viewmodel

import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

enum class RecordingState {
    STARTED, PAUSED, RESUMED, STOPPED
}

// 녹음기 상태 관리
@HiltViewModel
class RecorderViewModel @Inject constructor(

) : ViewModel() {
    private var mediaRecorder: MediaRecorder? = null
    private val _recordingState = MutableLiveData<RecordingState>(RecordingState.STOPPED)
    val recordingState: LiveData<RecordingState> = _recordingState
    private var recordingFilePath: String? = null

    // 녹음 시간
    private var recordingStartTime = 0L
    private val _recordingDuration = MutableLiveData<Long>(0)
    val recordingDuration: LiveData<Long> = _recordingDuration

    // 녹음 파형
    private val _amplitude = MutableLiveData<Int>()
    val amplitude: LiveData<Int> = _amplitude

    init {
        setupRecordingFilePath()
    }

    private fun setupRecordingFilePath() {
        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val bookingDir = File(downloadDir, "Booking")
        if (!bookingDir.exists()) {
            bookingDir.mkdirs() // "booking" 폴더가 없으면 생성
        }
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val recordingFileName = "booking_$timestamp.m4a"
        val recordingFile = File(bookingDir, recordingFileName)
        recordingFilePath = recordingFile.absolutePath
    }

    fun startRecording() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(recordingFilePath)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)
            }
            prepare()
        }
        recordingStartTime = System.currentTimeMillis()
        mediaRecorder?.start()
        startTimer()
        _recordingState.value = RecordingState.STARTED
    }

    fun pauseRecording() {
        mediaRecorder?.pause()
        _recordingState.value = RecordingState.PAUSED
        stopTimer()
    }

    fun resumeRecording() {
        mediaRecorder?.resume()
        _recordingState.value = RecordingState.RESUMED
        recordingStartTime = System.currentTimeMillis() - _recordingDuration.value!!
        startTimer()
    }

    fun stopRecording() {
        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null
        _recordingState.value = RecordingState.STOPPED
        if (recordingFilePath != null && checkFileExists(recordingFilePath)) {
            // 파일이 존재하는 경우, 다운로드 가능
        } else {
            // 파일이 없는 경우, 다운로드 불가능 알림
        }
        stopTimer()
    }

    private var timerJob: Job? = null
    private fun startTimer() {
        timerJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                val currentTime = System.currentTimeMillis()
                _recordingDuration.postValue(currentTime - recordingStartTime)
                _amplitude.postValue(mediaRecorder?.maxAmplitude ?: 0)
                delay(50)
            }
        }
    }
    private fun stopTimer() {
        timerJob?.cancel()
    }

    private fun checkFileExists(filePath: String?): Boolean {
        return filePath != null && File(filePath).exists()
    }

    fun convertMillisToTimeFormat(millis: Int): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onCleared() {
        super.onCleared()
        mediaRecorder?.release()
        _recordingState.value = RecordingState.STOPPED
    }
}