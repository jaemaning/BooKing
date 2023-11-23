package com.ssafy.data.repository

import com.ssafy.data.remote.api.BookingBoardApi
import com.ssafy.domain.model.booking.BookingBoardCreateRequest
import com.ssafy.domain.model.booking.BookingBoardCreateResponse
import com.ssafy.domain.model.booking.BookingBoardReadDetailResponse
import com.ssafy.domain.model.booking.BookingBoardReadListResponse
import com.ssafy.domain.model.booking.BookingBoardUpdateRequest
import com.ssafy.domain.model.booking.BookingBoardUpdateResponse
import com.ssafy.domain.repository.BookingBoardRepository
import retrofit2.Response
import javax.inject.Inject

class BookingBoardRepositoryImpl @Inject constructor(
    private val bookingBoardApi : BookingBoardApi
) : BookingBoardRepository {
    override suspend fun postBookingBoardCreate(request: BookingBoardCreateRequest): Response<Unit> {
        return bookingBoardApi.postBookingBoardCreate(request)
    }

    override suspend fun getBookingBoardReadList(meetingId: Long): Response<List<BookingBoardReadListResponse>> {
        return bookingBoardApi.getBookingBoardReadList(meetingId)
    }

    override suspend fun getBookingBoardReadDetail(postId: Long): Response<BookingBoardReadDetailResponse> {
        return bookingBoardApi.getBookingBoardReadDetail(postId)
    }

    override suspend fun patchBookingBoardUpdata(request: BookingBoardUpdateRequest): Response<BookingBoardUpdateResponse> {
        return bookingBoardApi.patchBookingBoardUpdate(request)
    }

    override suspend fun deleteBookingBoardDelete(postId: Long): Response<Unit> {
        return bookingBoardApi.deleteBookingBoardDelete(postId)
    }

}