package com.ssafy.data.repository.google

import com.ssafy.domain.model.google.AccountInfo
import com.ssafy.domain.repository.google.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val preferenceDatasource: PreferenceDataSource
) : AccountRepository {
    private val accountInfoFlow = MutableStateFlow(preferenceDatasource.getAccountInfo())
    override fun getAccountInfo(): StateFlow<AccountInfo?> {
        return accountInfoFlow
    }

    override suspend fun signInGoogle(accountInfo: AccountInfo) {
        preferenceDatasource.putAccountInfo(accountInfo)
        accountInfoFlow.emit(accountInfo)
    }

    override suspend fun signOutGoogle() {
        preferenceDatasource.removeAccountInfo()
        accountInfoFlow.emit(null)
    }
}