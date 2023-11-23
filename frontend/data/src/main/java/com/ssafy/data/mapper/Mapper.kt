package com.ssafy.data.mapper

import com.ssafy.data.remote.model.GoogleResponse

object Mapper {
    fun mapperGoogle(response: List<GoogleResponse>?) : List<com.ssafy.domain.model.GoogleResponse>? {
        return if (response != null){
            response.toDomain()
        } else null
    }

    fun List<GoogleResponse>.toDomain() : List<com.ssafy.domain.model.GoogleResponse> {
        return this.map {
            com.ssafy.domain.model.GoogleResponse(
                it.name,
                it.email
            )
        }
    }
}