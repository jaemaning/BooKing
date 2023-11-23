package com.ssafy.domain.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Inject
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

class OkhttpService @Inject constructor() {

    @Module
    @InstallIn(SingletonComponent::class)
    object OkHttpClientSingleton {
        @Provides
        @Singleton
        fun provideOkHttpClient() : OkHttpClient =
            OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build()
    }
}


