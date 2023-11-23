package com.ssafy.domain.usecase

import com.ssafy.domain.model.booksearch.BookSearchPopularResponse
import com.ssafy.domain.model.booksearch.BookSearchResponse
import com.ssafy.domain.repository.BookSearchRepository
import retrofit2.Response
import javax.inject.Inject

class BookSearchUseCase @Inject constructor(
    private val repository: BookSearchRepository
) {
    suspend fun getBookSearch(title: String) : Response<List<BookSearchResponse>> {
        return repository.getBookSearch(title)
    }

    suspend fun getBookLatest(page: Int, size: Int) : Response<List<BookSearchResponse>> {
        return repository.getBookLatest(page, size)
    }

    suspend fun getBookSearchByIsbn(isbn : String) : Response<BookSearchResponse> {
        return repository.getBookSearchByIsbn(isbn)
    }

    suspend fun getBookPopular() : Response<List<BookSearchPopularResponse>> {
        return repository.getBookPopular()
    }
}