package com.ssafy.data.repository

import com.ssafy.data.remote.api.NaverCloudApi
import com.ssafy.domain.repository.NaverCloudRepository
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class NaverCloudRepositoryImpl @Inject constructor(
    private val naverCloudApi: NaverCloudApi
) :NaverCloudRepository {
    override suspend fun getObject(bucketName: String, objectName: String): Response<ResponseBody> {
        return naverCloudApi.getObject(bucketName, objectName)
    }

    override suspend fun putObject(bucketName: String, objectName: String, file: RequestBody): Response<Unit> {
        return naverCloudApi.putObject(bucketName, objectName, file)
    }

    override suspend fun listObjects(
        bucketName: String,
        queryString: String
    ): Response<ResponseBody> {
        return naverCloudApi.listObjects(bucketName, queryString)
    }

}