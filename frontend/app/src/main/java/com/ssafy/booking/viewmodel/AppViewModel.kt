package com.ssafy.booking.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class AppViewModel(application: Application) : AndroidViewModel(application) {

    // 메인화면 진입시 초기화
    fun init() {
        viewModelScope.launch(Dispatchers.Main) {
        }
    }
}

class DummyAppViewModel : AppViewModel(Application())
