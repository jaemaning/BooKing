package com.ssafy.booking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.booking.BookingBoardCreateRequest
import com.ssafy.domain.model.booking.BookingBoardCreateResponse
import com.ssafy.domain.model.booking.BookingBoardReadDetailResponse
import com.ssafy.domain.model.booking.BookingBoardReadListResponse
import com.ssafy.domain.model.booking.BookingBoardUpdateRequest
import com.ssafy.domain.model.booking.BookingBoardUpdateResponse
import com.ssafy.domain.model.booksearch.BookSearchPopularResponse
import com.ssafy.domain.usecase.BookingBoardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class BookingBoardViewModel @Inject constructor(
    private val bookingBoardUseCase: BookingBoardUseCase
) :ViewModel() {

    // C
    private val _postBookingBoardCreateResponse = MutableLiveData<Response<Unit>>()
    val postBookingBoardCreateResponse: LiveData<Response<Unit>> get() = _postBookingBoardCreateResponse

    fun postBookingBoardCreate(request: BookingBoardCreateRequest) =
        viewModelScope.launch {
            _postBookingBoardCreateResponse.value = bookingBoardUseCase.postBookingBoardCreate(request)
        }

    // R - list
    private val _getBookingBoardReadListResponse = MutableLiveData<Response<List<BookingBoardReadListResponse>>>()
    val getBookingBoardReadListResponse: LiveData<Response<List<BookingBoardReadListResponse>>> get() = _getBookingBoardReadListResponse

    fun getBookingBoardReadList(meetingId : Long) =
        viewModelScope.launch {
            _getBookingBoardReadListResponse.value = bookingBoardUseCase.getBookingBoardReadList(meetingId)
        }

    // R - detail
    private val _getBookingBoardReadDetailResponse = MutableLiveData<Response<BookingBoardReadDetailResponse>>()
    val getBookingBoardReadDetailResponse: LiveData<Response<BookingBoardReadDetailResponse>> get() = _getBookingBoardReadDetailResponse

    fun getBookingBoardReadDetail(postId : Long) =
        viewModelScope.launch {
            _getBookingBoardReadDetailResponse.value = bookingBoardUseCase.getBookingBoardReadDetail(postId)
        }

    // U
    private val _patchBookingBoardUpdateResponse = MutableLiveData<Response<BookingBoardUpdateResponse>>()
    val patchBookingBoardUpdateResponse: LiveData<Response<BookingBoardUpdateResponse>> get() = _patchBookingBoardUpdateResponse

    fun patchBookingBoardUpdate(request: BookingBoardUpdateRequest) =
        viewModelScope.launch {
            _patchBookingBoardUpdateResponse.value = bookingBoardUseCase.patchBookingBoardUpdata(request)
        }

    // D
    private val _deleteBookingBoardDeleteResponse = MutableLiveData<Response<Unit>>()
    val deleteBookingBoardDeleteResponse: LiveData<Response<Unit>> get() = _deleteBookingBoardDeleteResponse

    fun deleteBookingBoardDelete(postId : Long) =
        viewModelScope.launch {
            _deleteBookingBoardDeleteResponse.value = bookingBoardUseCase.deleteBookingBoardDelete(postId)
        }
}