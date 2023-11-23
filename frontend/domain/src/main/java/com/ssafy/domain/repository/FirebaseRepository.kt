package com.ssafy.domain.repository

import com.ssafy.domain.model.DeviceToken
import retrofit2.Response

interface FirebaseRepository {
    suspend fun postDeviceToken(request: DeviceToken) : Response<Unit>
}