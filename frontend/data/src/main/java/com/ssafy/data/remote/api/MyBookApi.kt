package com.ssafy.data.remote.api

import com.ssafy.domain.model.mybook.MyBookListResponse
import com.ssafy.domain.model.mybook.MyBookMemoRegisterRequest
import com.ssafy.domain.model.mybook.MyBookRegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface MyBookApi {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/api/book/member/")
    suspend fun postBookRegister(@Body request: MyBookRegisterRequest) : Response<Unit>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/book/member/{memberPk}")
    suspend fun getBookList(@Path("memberPk") memberPk: Long) : Response<List<MyBookListResponse>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/book/member/{memberPk}/{isbn}")
    suspend fun getBookDetail(@Path("memberPk") memberPk: Long, @Path("isbn") isbn: String) : Response<MyBookListResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/api/book/member/note")
    suspend fun postBookMemo(@Body request: MyBookMemoRegisterRequest) : Response<Unit>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @DELETE("/api/book/member/{memberBookId}")
    suspend fun deleteBookRegister(@Path("memberBookId") memberBookId: String) : Response<Unit>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @DELETE("/api/book/member/{memberBookId}/{noteIndex}")
    suspend fun deleteBookNote(@Path("memberBookId") memberBookId: String, @Path("noteIndex") noteIndex: Int) : Response<Unit>
}