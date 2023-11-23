package com.ssafy.data.remote.api

import com.ssafy.domain.model.DeviceToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FirebaseApi {
    @Headers("Content-Type: application/json")
    @POST("/api/notification/init")
    suspend fun postDeviceToken(@Body request: DeviceToken): Response<Unit>
}