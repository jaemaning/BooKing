package com.ssafy.data.repository

import com.ssafy.data.remote.api.MemberApi
import com.ssafy.domain.model.SignInRequest
import com.ssafy.domain.repository.MemberRepository
import retrofit2.Response
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val memberApi: MemberApi
) :MemberRepository {
    override suspend fun signIn(request: SignInRequest): Response<String> {
        return memberApi.signIn(request)
    }
}