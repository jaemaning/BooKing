package com.ssafy.data.repository

import com.ssafy.data.remote.model.GoogleResponse

interface GoogleDataSource {
    suspend fun getGoogle() : List<GoogleResponse>?
}