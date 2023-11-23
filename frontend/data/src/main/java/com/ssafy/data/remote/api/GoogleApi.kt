package com.ssafy.data.remote.api

import retrofit2.Call
import retrofit2.http.GET
import com.ssafy.data.remote.model.GoogleResponse

interface GoogleApi {

    @GET("/oauth2/authorization/google")
    suspend fun getRepos() : Call<List<GoogleResponse>>
}