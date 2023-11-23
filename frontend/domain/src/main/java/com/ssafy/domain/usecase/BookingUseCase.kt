package com.ssafy.domain.usecase

import com.ssafy.domain.model.booking.BookingAcceptRequest
import com.ssafy.domain.model.booking.BookingAll
import com.ssafy.domain.model.booking.BookingAttendRequest
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
import com.ssafy.domain.repository.BookingRepository
import retrofit2.Response
import javax.inject.Inject

class BookingUseCase @Inject constructor(private val repository: BookingRepository) {
    suspend fun postBookingCreate(request: BookingCreateRequest): Response<Unit> {
        return repository.postBookingCreate(request)
    }

    suspend fun getAllBooking(): Response<List<BookingAll>> {
        return repository.getAllBooking()
    }

    suspend fun getEachBooking(meetingId: Long): Response<BookingDetail> {
        return repository.getEachBooking(meetingId)
    }

    suspend fun getParticipants(meetingId: Long): Response<List<BookingParticipants>> {
        return repository.getParticipants(meetingId)
    }

    suspend fun getWaitingList(meetingId: Long): Response<List<BookingWaiting>> {
        return repository.getWaitingList(meetingId)
    }

    suspend fun getSearchList(query: String, display: Int, start: Int, sort: String): Response<SearchResponse> {
        return repository.getSearchList(query, display, start, sort)
    }
    suspend fun postBookingJoin(meetingId: Long,request:BookingJoinRequest): Response<Unit> {
        return repository.postBookingJoin(meetingId,request)
    }
    suspend fun postBookingAccept(meetingId: Long,memberId:Int,request:BookingAcceptRequest): Response<Unit> {
        return repository.postBookingAccept(meetingId,memberId,request)
    }
    suspend fun postBookingStart(request : BookingStartRequest): Response<Unit> {
        return repository.postBookingStart(request)
    }

    suspend fun postBookingReject(meetingId: Long,memberId: Int,request: BookingRejectRequest) : Response<Unit> {
        return repository.postBookingReject(meetingId,memberId,request)
    }
    suspend fun patchBookingDetail(request:BookingModifyRequest): Response<Unit> {
        return repository.patchBookingDetail(request)
    }
    suspend fun postBookingExit(meetingId: Long): Response<Unit> {
        return repository.postBookingExit(meetingId)
    }
    suspend fun deleteBooking(meetingId: Long): Response<Unit> {
        return repository.deleteBooking(meetingId)
    }
    suspend fun patchBookingEnd(meetingId: Long): Response<Unit> {
        return repository.patchBookingEnd(meetingId)
    }
    suspend fun patchBookingAttend(request : BookingAttendRequest): Response<Unit> {
        return repository.patchBookingAttend(request)
    }
    suspend fun getBookingByHashtag(hashtagId:Long): Response<List<BookingAll>> {
        return repository.getBookingByHashtag(hashtagId)
    }
    suspend fun getBookingByMemberPk(memberPk:Long): Response<List<BookingListByMemberPk>> {
        return repository.getBookingByMemberPk(memberPk)
    }
    suspend fun getBookingByTitle(title:String): Response<List<BookingAll>> {
        return repository.getBookingByTitle(title)
    }
    suspend fun patchBookingRestart(meetingId: Long): Response<Unit> {
        return repository.patchBookingRestart(meetingId)
    }
    suspend fun patchPayment(meetingId: Long): Response<Unit> {
        return repository.patchPayment(meetingId)
    }
}

