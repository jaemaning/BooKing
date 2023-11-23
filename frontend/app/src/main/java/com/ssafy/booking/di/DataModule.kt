package com.ssafy.booking.di

import com.ssafy.data.repository.google.AccountRepositoryImpl
import com.ssafy.domain.repository.google.AccountRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindSearchRepository(accountRepositoryImpl: AccountRepositoryImpl): AccountRepository
}
