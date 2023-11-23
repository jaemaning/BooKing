package com.ssafy.domain.usecase

import com.ssafy.domain.model.SignInRequest
import com.ssafy.domain.repository.MemberRepository
import retrofit2.Response
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val repository: MemberRepository) {
    suspend fun execute(request: SignInRequest): Response<String> {
        return repository.signIn(request)
    }
}