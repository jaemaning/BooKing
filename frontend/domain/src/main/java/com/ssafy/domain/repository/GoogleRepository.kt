package com.ssafy.domain.repository

import com.ssafy.domain.model.GoogleResponse

interface GoogleRepository {
    suspend fun getGoogle() : List<GoogleResponse>?
}