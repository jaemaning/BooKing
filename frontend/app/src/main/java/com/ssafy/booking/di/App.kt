package com.ssafy.booking.di

import android.annotation.SuppressLint
import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.naver.maps.map.NaverMapSdk
import com.ssafy.booking.BuildConfig
import com.ssafy.data.repository.token.TokenDataSource
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    companion object {
        private lateinit var application: App
        fun getInstance(): App = application

        lateinit var prefs: TokenDataSource
    }
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.kakaoSdkApp_key)
        application = this
        prefs = TokenDataSource(applicationContext)
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.naverMapClient_id)
    }
}
