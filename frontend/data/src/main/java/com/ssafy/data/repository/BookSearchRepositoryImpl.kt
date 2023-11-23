package com.ssafy.data.repository

import com.ssafy.data.remote.api.BookSearchApi
import com.ssafy.domain.model.booksearch.BookSearchPopularResponse
import com.ssafy.domain.model.booksearch.BookSearchResponse
import com.ssafy.domain.repository.BookSearchRepository
import retrofit2.Response
import javax.inject.Inject

class BookSearchRepositoryImpl @Inject constructor(
    private val bookSearchApi: BookSearchApi
) :BookSearchRepository {

    override suspend fun getBookSearch(title: String): Response<List<BookSearchResponse>> {
        return bookSearchApi.getBookSearch(title)
    }

    override suspend fun getBookLatest(page: Int, size: Int): Response<List<BookSearchResponse>> {
        return bookSearchApi.getBookLatest(page, size)
    }

    override suspend fun getBookSearchByIsbn(isbn: String): Response<BookSearchResponse> {
        return bookSearchApi.getBookSearchByIsbn(isbn)
    }

    override suspend fun getBookPopular(): Response<List<BookSearchPopularResponse>> {
        return bookSearchApi.getBookPopular()
    }
}