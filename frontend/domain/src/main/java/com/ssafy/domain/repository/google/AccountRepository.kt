package com.ssafy.domain.repository.google

import com.ssafy.domain.model.google.AccountInfo
import kotlinx.coroutines.flow.StateFlow

interface AccountRepository {
    fun getAccountInfo() : StateFlow<AccountInfo?>

    suspend fun signInGoogle(accountInfo: AccountInfo)
    suspend fun signOutGoogle()
}