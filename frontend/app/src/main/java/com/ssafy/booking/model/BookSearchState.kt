package com.ssafy.booking.model

import com.ssafy.domain.model.booksearch.BookSearchResponse

sealed class BookSearchState {
    object Initialize : BookSearchState()
    object Loading : BookSearchState()
    data class Success(val data: List<BookSearchResponse>) : BookSearchState()
    data class Error(val message: String) : BookSearchState()
}
