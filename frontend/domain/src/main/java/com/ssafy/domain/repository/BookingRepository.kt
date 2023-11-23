package com.ssafy.domain.repository

import com.ssafy.domain.model.booking.BookingAcceptRequest
import com.ssafy.domain.model.booking.BookingAll
import com.ssafy.domain.model.booking.BookingAttendRequest
import retrofit2.Response
import com.ssafy.domain.model.booking.BookingCreateRequest
import com.ssafy.domain.model.booking.BookingDetail
import com.ssafy.domain.model.booking.BookingJoinRequest
import com.ssafy.domain.model.booking.BookingListByHashtag
import com.ssafy.domain.model.booking.BookingListByMemberPk
import com.ssafy.domain.model.booking.BookingListByTitle
import com.ssafy.domain.model.booking.BookingModifyRequest
import com.ssafy.domain.model.booking.BookingParticipants
import com.ssafy.domain.model.booking.BookingRejectRequest
import com.ssafy.domain.model.booking.BookingStartRequest
import com.ssafy.domain.model.booking.BookingWaiting
import com.ssafy.domain.model.booking.SearchResponse

interface BookingRepository {
    suspend fun postBookingCreate(request : BookingCreateRequest) : Response<Unit>
    suspend fun getAllBooking() : Response<List<BookingAll>>
    suspend fun getEachBooking(meetingId:Long) : Response<BookingDetail>
    suspend fun getParticipants(meetingId:Long) : Response<List<BookingParticipants>>
    suspend fun getWaitingList(meetingId:Long) : Response<List<BookingWaiting>>
    suspend fun getSearchList(query:String,display:Int,start:Int,sort:String) : Response<SearchResponse>
    suspend fun postBookingJoin(meetingId: Long,request : BookingJoinRequest) : Response<Unit>
    suspend fun postBookingAccept(meetingId: Long,memberId:Int,request: BookingAcceptRequest) : Response<Unit>
    suspend fun postBookingReject(meetingId: Long,memberId:Int,request:BookingRejectRequest) : Response<Unit>
    suspend fun postBookingStart(request: BookingStartRequest) : Response<Unit>
    suspend fun patchBookingDetail(request: BookingModifyRequest) : Response<Unit>
    suspend fun postBookingExit(meetingId: Long) : Response<Unit>
    suspend fun deleteBooking(meetingId: Long) : Response<Unit>
    suspend fun patchBookingEnd(meetingId: Long) : Response<Unit>
    suspend fun patchBookingAttend(request : BookingAttendRequest) : Response<Unit>
    suspend fun getBookingByHashtag(hashtagId:Long) : Response<List<BookingAll>>
    suspend fun getBookingByMemberPk(memberPk:Long) : Response<List<BookingListByMemberPk>>
    suspend fun getBookingByTitle(title:String) : Response<List<BookingAll>>
    suspend fun patchBookingRestart(meetingId: Long) : Response<Unit>
    suspend fun patchPayment(meetingId: Long) : Response<Unit>
}