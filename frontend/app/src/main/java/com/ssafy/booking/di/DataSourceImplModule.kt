package com.ssafy.booking.di

import com.ssafy.data.remote.api.GoogleApi
import com.ssafy.data.repository.GoogleDataSource
import com.ssafy.data.repository.GoogleDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceImplModule {

    @Provides
    @Singleton
    fun provideMainDataSource(
        googleApi: GoogleApi
    ): GoogleDataSource {
        return GoogleDataSourceImpl(
            googleApi
        )
    }
}
