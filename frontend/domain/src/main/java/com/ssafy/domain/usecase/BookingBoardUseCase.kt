package com.ssafy.domain.usecase

import com.ssafy.domain.model.booking.BookingBoardCreateRequest
import com.ssafy.domain.model.booking.BookingBoardCreateResponse
import com.ssafy.domain.model.booking.BookingBoardReadDetailResponse
import com.ssafy.domain.model.booking.BookingBoardReadListResponse
import com.ssafy.domain.model.booking.BookingBoardUpdateRequest
import com.ssafy.domain.model.booking.BookingBoardUpdateResponse
import com.ssafy.domain.repository.BookingBoardRepository
import retrofit2.Response
import javax.inject.Inject

class BookingBoardUseCase @Inject constructor(
    private val repository: BookingBoardRepository
) {

    suspend fun postBookingBoardCreate(request: BookingBoardCreateRequest) : Response<Unit> {
        return repository.postBookingBoardCreate(request)
    }
    suspend fun getBookingBoardReadList(meetingId : Long) : Response<List<BookingBoardReadListResponse>> {
        return repository.getBookingBoardReadList(meetingId)
    }
    suspend fun getBookingBoardReadDetail(postId : Long) : Response<BookingBoardReadDetailResponse> {
        return repository.getBookingBoardReadDetail(postId)
    }
    suspend fun patchBookingBoardUpdata(request: BookingBoardUpdateRequest) : Response<BookingBoardUpdateResponse> {
        return repository.patchBookingBoardUpdata(request)
    }
    suspend fun deleteBookingBoardDelete(postId : Long) : Response<Unit> {
        return repository.deleteBookingBoardDelete(postId)
    }
}