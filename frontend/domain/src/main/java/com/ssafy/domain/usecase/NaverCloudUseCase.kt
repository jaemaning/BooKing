package com.ssafy.domain.usecase

import android.net.Uri
import com.ssafy.domain.repository.NaverCloudRepository
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class NaverCloudUseCase @Inject constructor(
    private val repository: NaverCloudRepository
) {
    suspend fun getObject(bucketName: String, objectName: String) : Response<ResponseBody> {
        return repository.getObject(bucketName, objectName)
    }

    suspend fun putObject(bucketName: String, objectName: String, file: RequestBody) : Response<Unit> {
        return repository.putObject(bucketName, objectName, file)
    }

    suspend fun listObjects(bucketName: String, queryString: String) : Response<ResponseBody> {
        return repository.listObjects(bucketName, queryString)
    }
}