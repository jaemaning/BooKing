package com.ssafy.domain.repository

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

interface NaverCloudRepository {
    suspend fun getObject(bucketName: String, objectName: String) : Response<ResponseBody>

    suspend fun putObject(bucketName: String, objectName: String, file: RequestBody) : Response<Unit>

    suspend fun listObjects(bucketName: String, queryString: String) : Response<ResponseBody>
}