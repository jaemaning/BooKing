package com.ssafy.data.repository

import com.ssafy.data.remote.api.FirebaseApi
import com.ssafy.domain.model.DeviceToken
import com.ssafy.domain.repository.FirebaseRepository
import retrofit2.Response
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(private val firebaseApi: FirebaseApi) : FirebaseRepository {
    override suspend fun postDeviceToken(request: DeviceToken): Response<Unit> {
        return firebaseApi.postDeviceToken(request)
    }
}