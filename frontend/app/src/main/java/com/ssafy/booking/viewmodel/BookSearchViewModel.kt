package com.ssafy.booking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.booking.model.BookSearchState
import com.ssafy.domain.model.booksearch.BookSearchPopularResponse
import com.ssafy.domain.model.booksearch.BookSearchResponse
import com.ssafy.domain.usecase.BookSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(
    private val bookSearchUseCase: BookSearchUseCase
) : ViewModel() {

    // 검색어를 bookTitle 로 받음
    private val _bookTitle = MutableStateFlow("")
    val bookTitle: StateFlow<String> = _bookTitle.asStateFlow()

    // title 값을 변경하는 메서드
    fun setBookTitle(newTitle: String) {
        _bookTitle.value = newTitle
    }

    // 도서 조회 상태 핸들링
    private val _bookSearchState = MutableLiveData<BookSearchState>()
    val bookSearchState: LiveData<BookSearchState> = _bookSearchState

    fun bookSearchStateToInit() =
        viewModelScope.launch {
            _bookSearchState.value = BookSearchState.Initialize
        }

    fun bookSearchStateToLoading() =
        viewModelScope.launch {
            _bookSearchState.value = BookSearchState.Loading
        }

    // GET - 도서 조회
    private val _getBookSearchResponse = MutableLiveData<Response<List<BookSearchResponse>>>()
    val getBookSearchResponse: LiveData<Response<List<BookSearchResponse>>> get() = _getBookSearchResponse

    fun getBookSearch(title: String) =
        viewModelScope.launch {
            _getBookSearchResponse.value = bookSearchUseCase.getBookSearch(title)
        }


    // GET - 최신 도서 조회
    private val _getBookLatestResponse = MutableLiveData<Response<List<BookSearchResponse>>>()
    val getBookLatestResponse: LiveData<Response<List<BookSearchResponse>>> get() = _getBookLatestResponse

    fun getBookLatest(page: Int, size: Int) =
        viewModelScope.launch {
            _getBookLatestResponse.value = bookSearchUseCase.getBookLatest(page, size)
        }

    // GET - 인기 도서 조회
    private val _getBookPopularResponse = MutableLiveData<Response<List<BookSearchPopularResponse>>>()
    val getBookPopularResponse: LiveData<Response<List<BookSearchPopularResponse>>> get() = _getBookPopularResponse

    fun getBookPopular() =
        viewModelScope.launch {
            _getBookPopularResponse.value = bookSearchUseCase.getBookPopular()
        }

    // GET - isbn 으로 도서 조회
    private val _getBookSearchByIsbnResponse = MutableLiveData<Response<BookSearchResponse>>()
    val getBookSearchByIsbnResponse: LiveData<Response<BookSearchResponse>> get() = _getBookSearchByIsbnResponse

    fun getBookSearchByIsbn(isbn: String) =
        viewModelScope.launch {
            _getBookSearchByIsbnResponse.value = bookSearchUseCase.getBookSearchByIsbn(isbn)
        }

    fun bookSearch(title: String) =
        viewModelScope.launch {
            _bookSearchState.value = BookSearchState.Loading

            try {
                val bookSearchResponse = bookSearchUseCase.getBookSearch(title)
                if (bookSearchResponse.isSuccessful) {
                    val successBookSearchResponse = bookSearchResponse.body()
                    _bookSearchState.value = BookSearchState.Success(successBookSearchResponse!!)
                } else {
                    _bookSearchState.value = BookSearchState.Error("Failed to Search the book from server.")
                    return@launch
                }
            } catch (e: Exception) {
                _bookSearchState.value = BookSearchState.Error(e.message ?: "Unknown error occurred")
            }
        }
}
