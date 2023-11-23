package com.ssafy.domain.usecase.google

import com.ssafy.domain.model.google.AccountInfo
import com.ssafy.domain.repository.google.AccountRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    fun getAccountInfo() : StateFlow<AccountInfo?>{
        return accountRepository.getAccountInfo()
    }

    suspend fun signInGoogle(accountInfo: AccountInfo) {
        accountRepository.signInGoogle(accountInfo)
    }
    suspend fun signOutGoogle() {
        accountRepository.signOutGoogle()
    }

}