package com.ssafy.data.repository

import com.ssafy.data.remote.api.GoogleApi
import com.ssafy.data.remote.model.GoogleResponse

import javax.inject.Inject

class GoogleDataSourceImpl @Inject constructor(
    private val googleApi: GoogleApi
) : GoogleDataSource {
    override suspend fun getGoogle(): List<GoogleResponse>? {
        val response = googleApi.getRepos().execute()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

}