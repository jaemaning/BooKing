package com.ssafy.data.repository

import com.ssafy.data.mapper.Mapper
import com.ssafy.domain.model.GoogleResponse
import com.ssafy.domain.repository.GoogleRepository
import javax.inject.Inject

class GoogleRepositoryImpl @Inject constructor(
    private val googleDataSource: GoogleDataSource
) : GoogleRepository {
    override suspend fun getGoogle(): List<GoogleResponse>? {
        return Mapper.mapperGoogle(googleDataSource.getGoogle())
    }
}