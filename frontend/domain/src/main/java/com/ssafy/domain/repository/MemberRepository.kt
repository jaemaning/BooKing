package com.ssafy.domain.repository

import com.ssafy.domain.model.SignInRequest
import retrofit2.Response

interface MemberRepository {
    suspend fun signIn(request: SignInRequest): Response<String>
}