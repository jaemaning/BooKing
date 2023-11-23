package com.ssafy.domain.usecase

import com.ssafy.domain.repository.GoogleRepository
import javax.inject.Inject

class GetTokenRepoUseCase @Inject constructor(
    private val googleRepository: GoogleRepository
) {
    suspend fun execute() = googleRepository.getGoogle()
}