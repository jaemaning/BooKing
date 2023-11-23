package com.ssafy.data.remote.api

import com.ssafy.domain.model.booking.BookingBoardCreateRequest
import com.ssafy.domain.model.booking.BookingBoardCreateResponse
import com.ssafy.domain.model.booking.BookingBoardReadDetailResponse
import com.ssafy.domain.model.booking.BookingBoardReadListResponse
import com.ssafy.domain.model.booking.BookingBoardUpdateRequest
import com.ssafy.domain.model.booking.BookingBoardUpdateResponse
import com.ssafy.domain.model.booking.BookingCreateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface BookingBoardApi {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/api/booking/meeting/post/")
    suspend fun postBookingBoardCreate(@Body request: BookingBoardCreateRequest): Response<Unit>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/booking/meeting/{meetingId}/post")
    suspend fun getBookingBoardReadList(@Path("meetingId") meetingId : Long) : Response<List<BookingBoardReadListResponse>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/booking/meeting/post/{postId}")
    suspend fun getBookingBoardReadDetail(@Path("postId") postId : Long) : Response<BookingBoardReadDetailResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @PATCH("/api/booking/meeting/post")
    suspend fun patchBookingBoardUpdate(@Body request: BookingBoardUpdateRequest) : Response<BookingBoardUpdateResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @DELETE("/api/booking/meeting/post/{postId}")
    suspend fun deleteBookingBoardDelete(@Path("postId") postId: Long) : Response<Unit>
}