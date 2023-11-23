package com.ssafy.data.remote.api
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
import com.ssafy.domain.model.mypage.UserInfoResponse
import retrofit2.Response
import com.ssafy.data.BuildConfig

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BookingApi {
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/api/booking/meeting/")
    suspend fun postBookingCreate(@Body request: BookingCreateRequest): Response<Unit>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/booking/meeting/")
    suspend fun getAllBooking(): Response<List<BookingAll>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/booking/meeting/detail/{meetingId}")
    suspend fun getEachBooking(@Path("meetingId") meetingId: Long): Response<BookingDetail>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/booking/participant/{meetingId}")
    suspend fun getParticipants(@Path("meetingId") meetingId: Long): Response<List<BookingParticipants>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/booking/waitlist/{meetingId}")
    suspend fun getWaitingList(@Path("meetingId") meetingId: Long): Response<List<BookingWaiting>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/api/booking/meeting/{meetingId}/waiting")
    suspend fun postBookingJoin(@Path("meetingId") meetingId: Long,@Body request : BookingJoinRequest): Response<Unit>
//    suspend fun postBookingJoin(@Body request : BookingJoinRequest): Response<Unit>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/api/booking/meeting/{meetingId}/accept/{memberId}")
//    suspend fun postBookingAccept(@Body request: BookingAcceptRequest): Response<Unit>
    suspend fun postBookingAccept(@Path("meetingId") meetingId: Long,@Path("memberId") memberId: Int,@Body request: BookingAcceptRequest): Response<Unit>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/api/booking/meeting/{meetingId}/reject/{memberId}")
    suspend fun postBookingReject(@Path("meetingId") meetingId: Long,@Path("memberId") memberId: Int,@Body request: BookingRejectRequest): Response<Unit>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/api/booking/meeting/info/")
    suspend fun postBookingStart(@Body request: BookingStartRequest): Response<Unit>


    // 네이버 지역 검색 API
    @Headers("X-Naver-Client-Id: ${BuildConfig.naverClient_id}", "X-Naver-Client-Secret: ${BuildConfig.naverClient_secret}")
    // 상대경로->BASE_URL 사용 , 절대경로시 무시하고 절대경로 사용.
    @GET("https://openapi.naver.com/v1/search/local.json")
    suspend fun getSearchList(
        @Query("query") query: String,
        @Query("display") display: Int, // display는 일반적으로 숫자 값이므로 Int로 변경, 이거 안 되면 String
        @Query("start") start: Int,    // start도 마찬가지로 Int 타입이 적합, 이거 안 되면 String
        @Query("sort") sort: String
    ): Response<SearchResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @PATCH("/api/booking/meeting/")
    suspend fun patchBookingDetail(@Body request: BookingModifyRequest): Response<Unit>
    
    // 모임 나가기
    @Headers("Content-Type: application/json;charset=UTF-8")
    @DELETE("/api/booking/meeting/{meetingId}/exit")
    suspend fun postBookingExit(@Path("meetingId") meetingId: Long): Response<Unit>
    
    // 모임 삭제
    @Headers("Content-Type: application/json;charset=UTF-8")
    @DELETE("/api/booking/meeting/{meetingId}")
    suspend fun deleteBooking(@Path("meetingId") meetingId: Long): Response<Unit>
    
    // 모임 종료
    @Headers("Content-Type: application/json;charset=UTF-8")
    @PATCH("/api/booking/meeting/finish/{meetingId}")
    suspend fun patchBookingEnd(@Path("meetingId") meetingId: Long): Response<Unit>
    
    // 모임 출석체크
    @Headers("Content-Type: application/json;charset=UTF-8")
    @PATCH("/api/booking/meeting/attend")
    suspend fun patchBookingAttend(@Body request: BookingAttendRequest): Response<Unit>

    // 모임 목록 조회 - 해시태그
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/booking/meeting/hashtag/{hashtagId}")
    suspend fun getBookingByHashtag(@Path("hashtagId") hashtagId: Long): Response<List<BookingAll>>

    // 모임 목록 조회 - memberPk
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/booking/meeting/member/{memberPk}")
    suspend fun getBookingByMemberPk(@Path("memberPk") memberPk: Long): Response<List<BookingListByMemberPk>>

    // 모임 목록 조회 - title
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/booking/meeting/title/{title}")
    suspend fun getBookingByTitle(@Path("title") title: String): Response<List<BookingAll>>

    // 모임 한 번 더 하기
    @Headers("Content-Type: application/json;charset=UTF-8")
    @PATCH("/api/booking/meeting/restart/{meetingId}")
    suspend fun patchBookingRestart(@Path("meetingId") meetingId: Long): Response<Unit>

    // 결제하기
    @Headers("Content-Type: application/json;charset=UTF-8")
    @PATCH("/api/booking/meeting/pay/{meetingId}")
    suspend fun patchPayment(@Path("meetingId") meetingId: Long): Response<Unit>

}
