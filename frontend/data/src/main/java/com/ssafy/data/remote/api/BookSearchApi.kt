package com.ssafy.data.remote.api

import com.ssafy.domain.model.booksearch.BookSearchPopularResponse
import com.ssafy.domain.model.booksearch.BookSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface BookSearchApi {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/book/searchByTitle")
    suspend fun getBookSearch(@Query("title") title: String) : Response<List<BookSearchResponse>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/book/searchByIsbn")
    suspend fun getBookSearchByIsbn(@Query("isbn") isbn: String) : Response<BookSearchResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/book/latest")
    suspend fun getBookLatest(
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Response<List<BookSearchResponse>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/book/popular")
    suspend fun getBookPopular() : Response<List<BookSearchPopularResponse>>

}