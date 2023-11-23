package com.ssafy.domain.repository

import com.ssafy.domain.model.booksearch.BookSearchPopularResponse
import com.ssafy.domain.model.booksearch.BookSearchResponse
import retrofit2.Response

interface BookSearchRepository {

    suspend fun getBookSearch(title: String) : Response<List<BookSearchResponse>>

    suspend fun getBookLatest(page: Int, size: Int) : Response<List<BookSearchResponse>>

    suspend fun getBookSearchByIsbn(isbn: String) : Response<BookSearchResponse>

    suspend fun getBookPopular() : Response<List<BookSearchPopularResponse>>
}