package com.ssafy.domain.repository

import com.ssafy.domain.model.booking.BookingBoardCreateRequest
import com.ssafy.domain.model.booking.BookingBoardReadDetailResponse
import com.ssafy.domain.model.booking.BookingBoardReadListResponse
import com.ssafy.domain.model.booking.BookingBoardUpdateRequest
import com.ssafy.domain.model.booking.BookingBoardUpdateResponse
import retrofit2.Response

interface BookingBoardRepository {

    suspend fun postBookingBoardCreate(request: BookingBoardCreateRequest) : Response<Unit>
    suspend fun getBookingBoardReadList(meetingId : Long) : Response<List<BookingBoardReadListResponse>>
    suspend fun getBookingBoardReadDetail(postId : Long) : Response<BookingBoardReadDetailResponse>
    suspend fun patchBookingBoardUpdata(request: BookingBoardUpdateRequest) : Response<BookingBoardUpdateResponse>
    suspend fun deleteBookingBoardDelete(postId : Long) : Response<Unit>
}