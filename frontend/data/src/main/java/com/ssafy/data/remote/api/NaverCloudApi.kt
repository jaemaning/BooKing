package com.ssafy.data.remote.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NaverCloudApi {
    @Headers("Host: kr.object.ncloudstorage.com")
    @GET("/{bucketName}/{objectName}")
    suspend fun getObject(
        @Path("bucketName") bucketName: String,
        @Path("objectName") objectName: String
    ): Response<ResponseBody>

    @Headers("Host: kr.object.ncloudstorage.com")
    @PUT("/{bucketName}/{objectName}")
    suspend fun putObject(
        @Path("bucketName") bucketName: String,
        @Path("objectName") objectName: String,
        @Body file: RequestBody
    ): Response<Unit>

    @Headers("Host: kr.object.ncloudstorage.com")
    @GET("/{bucketName}")
    suspend fun listObjects(
        @Path("bucketName") bucketName: String,
        @Query("queryString") queryString: String
    ): Response<ResponseBody>
}