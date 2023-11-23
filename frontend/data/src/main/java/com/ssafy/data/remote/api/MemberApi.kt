package com.ssafy.data.remote.api

import com.ssafy.domain.model.SignInRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MemberApi {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/api/members/signup")
    suspend fun signIn(@Body request: SignInRequest): Response<String>
}