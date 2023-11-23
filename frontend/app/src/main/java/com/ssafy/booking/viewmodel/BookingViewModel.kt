package com.ssafy.booking.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.data.repository.FirebaseRepositoryImpl
import com.ssafy.domain.model.DeviceToken
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
import com.ssafy.domain.usecase.BookingUseCase
import com.ssafy.domain.usecase.MyPageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingUseCase: BookingUseCase,
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl,
    private val myPageUseCase: MyPageUseCase
) : ViewModel() {

    // POST - 모임 생성
    private val _postCreateBookingResponse = MutableLiveData<Response<Unit>>()
    private val _createBookingSuccess = MutableLiveData<Boolean?>()
    val createBookingSuccess: LiveData<Boolean?> get() = _createBookingSuccess
    val postCreateBookingResponse: LiveData<Response<Unit>> get() = _postCreateBookingResponse
    fun postCreateBooking(request: BookingCreateRequest) =
        viewModelScope.launch {
            val response = bookingUseCase.postBookingCreate(request)
            _postCreateBookingResponse.value = response
            _createBookingSuccess.value = response.isSuccessful // 여기서 모임생성의 성공 여부를 업데이트
        }

    // 모임 생성 여부 초기화
    fun resetCreateBookingSuccess() {
        _createBookingSuccess.value = null
    }

    // POST - 디바이스 토큰 전송
    fun postDeivceToken(deviceToken: DeviceToken) =
        viewModelScope.launch {
            try {
                val response = firebaseRepositoryImpl.postDeviceToken(deviceToken)
                if (response.isSuccessful) {
                    Log.d("DEVICE_TOKEN", "SUCCESS $response")
                } else {
                    Log.d("DEVICE_TOKEN", "ELSE $response")
                }
            } catch (e: Exception) {
                Log.e("DEVICE_TOKEN", "ERROR")
            }
        }

    // GET - 모임 전체 목록 조회
    private val _getBookingAllListResponse = MutableLiveData<Response<List<BookingAll>>>()
    val getBookingAllList: LiveData<Response<List<BookingAll>>> get() = _getBookingAllListResponse
    fun getBookingAllList() =
        viewModelScope.launch {
            _getBookingAllListResponse.value = bookingUseCase.getAllBooking()
        }
    // GET - 모임 상세 조회
    private val _getBookingDetailResponse = MutableLiveData<Response<BookingDetail>>()
    val getBookingDetailResponse: LiveData<Response<BookingDetail>> get() = _getBookingDetailResponse
    fun getBookingDetail(meetingId: Long) =
        viewModelScope.launch {
            _getBookingDetailResponse.value = bookingUseCase.getEachBooking(meetingId)
        }

    // GET - 참여자 목록 조회
    private val _getParticipantsResponse = MutableLiveData<Response<List<BookingParticipants>>>()
    val getParticipantsResponse: LiveData<Response<List<BookingParticipants>>> get() = _getParticipantsResponse
    fun getParticipants(meetingId: Long) =
        viewModelScope.launch {
            _getParticipantsResponse.value = bookingUseCase.getParticipants(meetingId)
        }

    // GET - 대기자 목록 조회
    private val _getWaitingListResponse = MutableLiveData<Response<List<BookingWaiting>>>()
    val getWaitingListResponse: LiveData<Response<List<BookingWaiting>>> get() = _getWaitingListResponse
    fun getWaitingList(meetingId: Long) =
        viewModelScope.launch {
            _getWaitingListResponse.value = bookingUseCase.getWaitingList(meetingId)
        }

    // GET - 유저 정보 요청 로직
    private val _getUserInfoResponse = MutableLiveData<Response<UserInfoResponse>>()
    val getUserInfoResponse: LiveData<Response<UserInfoResponse>> get() = _getUserInfoResponse

    fun getUserInfo(loginId: String) =
        viewModelScope.launch {
            _getUserInfoResponse.value = myPageUseCase.getUserInfo(loginId)
        }

    // GET - 네이버 검색 API
    private val _getSearchListResponse = MutableLiveData<Response<SearchResponse>>()
    val getSearchListResponse: LiveData<Response<SearchResponse>> get() = _getSearchListResponse
    fun getSearchList(query: String, display: Int, start: Int, sort: String) =
        viewModelScope.launch {
            _getSearchListResponse.value = bookingUseCase.getSearchList(query, display, start, sort)
        }

    // POST - 모임 참여
    private val _postBookingJoinResponse = MutableLiveData<Response<Unit>>()
    val postBookingJoinResponse: LiveData<Response<Unit>> get() = _postBookingJoinResponse
    fun postBookingJoin(meetingId: Long,request: BookingJoinRequest) =
        viewModelScope.launch {
            _postBookingJoinResponse.value = bookingUseCase.postBookingJoin(meetingId,request)
        }

    // POST - 모임 참여 수락
    private val _postBookingAcceptResponse = MutableLiveData<Response<Unit>>()
    val postBookingAcceptResponse: LiveData<Response<Unit>> get() = _postBookingAcceptResponse
    fun postBookingAccept(meetingId: Long,memberId:Int,request: BookingAcceptRequest) =
        viewModelScope.launch {
            _postBookingAcceptResponse.value = bookingUseCase.postBookingAccept(meetingId,memberId,request)
        }

    // POST - 모임 참여 거절
private val _postBookingRejectResponse = MutableLiveData<Response<Unit>>()
    val postBookingRejectResponse: LiveData<Response<Unit>> get() = _postBookingRejectResponse
    fun postBookingReject(meetingId: Long,memberId:Int,request: BookingRejectRequest) =
        viewModelScope.launch {
            _postBookingRejectResponse.value = bookingUseCase.postBookingReject(meetingId,memberId,request)
        }

    // POST - 모임 시작
    private val _postBookingStartResponse = MutableLiveData<Response<Unit>>()
    val postBookingStartResponse: LiveData<Response<Unit>> get() = _postBookingStartResponse
    fun postBookingStart(request: BookingStartRequest) =
        viewModelScope.launch {
            _postBookingStartResponse.value = bookingUseCase.postBookingStart(request)
        }

    // POST - 모임 세팅
    val title = MutableLiveData("")
    val description = MutableLiveData("")
    val maxParticipants = MutableLiveData(2)
    val lgt = MutableLiveData(0.0)
    val lat = MutableLiveData(0.0)
    val location = MutableLiveData("")
    val placeName = MutableLiveData("")
    val date = MutableLiveData<LocalDate>()
    val time = MutableLiveData<LocalTime>()
    val fee = MutableLiveData(0)

    // PATCH - 모임 수정
    private val _patchBookingDetailResponse = MutableLiveData<Response<Unit>>()
    val patchBookingDetailResponse: LiveData<Response<Unit>> get() = _patchBookingDetailResponse
    fun patchBookingDetail(request: BookingModifyRequest) =
        viewModelScope.launch {
            _patchBookingDetailResponse.value = bookingUseCase.patchBookingDetail(request)
        }

    // POST - 모임 나가기
    private val _postBookingExitResponse = MutableLiveData<Response<Unit>>()
    val postBookingExitResponse: LiveData<Response<Unit>> get() = _postBookingExitResponse
    fun postBookingExit(meetingId: Long) =
        viewModelScope.launch {
            _postBookingExitResponse.value = bookingUseCase.postBookingExit(meetingId)
        }

    // DELETE - 모임 삭제
    private val _deleteBookingResponse = MutableLiveData<Response<Unit>>()
    val deleteBookingResponse: LiveData<Response<Unit>> get() = _deleteBookingResponse
    fun deleteBooking(meetingId: Long) =
        viewModelScope.launch {
            _deleteBookingResponse.value = bookingUseCase.deleteBooking(meetingId)
        }

    // PATCH - 모임 종료
    private val _patchBookingEndResponse = MutableLiveData<Response<Unit>>()
    val patchBookingEndResponse: LiveData<Response<Unit>> get() = _patchBookingEndResponse
    fun patchBookingEnd(meetingId: Long) =
        viewModelScope.launch {
            _patchBookingEndResponse.value = bookingUseCase.patchBookingEnd(meetingId)
        }

    // PATCH - 모임 출석
    private val _patchBookingAttendResponse = MutableLiveData<Response<Unit>>()
    val patchBookingAttendResponse: LiveData<Response<Unit>> get() = _patchBookingAttendResponse
    fun patchBookingAttend(request: BookingAttendRequest) =
        viewModelScope.launch {
            _patchBookingAttendResponse.value = bookingUseCase.patchBookingAttend(request)
        }

    // GET - 해시태그로 모임 목록 조회
    private val _getBookingByHashtagResponse = MutableLiveData<Response<List<BookingAll>>>()
    val getBookingByHashtagResponse: LiveData<Response<List<BookingAll>>> get() = _getBookingByHashtagResponse
    fun getBookingByHashtag(hashtagId: Long) =
        viewModelScope.launch {
            _getBookingByHashtagResponse.value = bookingUseCase.getBookingByHashtag(hashtagId)
        }

    // GET - 유저 pk로 모임 목록 조회
    private val _getBookingByMemberPkResponse = MutableLiveData<Response<List<BookingListByMemberPk>>>()
    val getBookingByMemberPkResponse: LiveData<Response<List<BookingListByMemberPk>>> get() = _getBookingByMemberPkResponse
    fun getBookingByMemberPk(memberPk: Long) =
        viewModelScope.launch {
            _getBookingByMemberPkResponse.value = bookingUseCase.getBookingByMemberPk(memberPk)
        }

    // GET - 제목으로 모임 목록 조회
    private val _getBookingByTitleResponse = MutableLiveData<Response<List<BookingAll>>>()
    val getBookingByTitleResponse: LiveData<Response<List<BookingAll>>> get() = _getBookingByTitleResponse
    fun getBookingByTitle(title: String) =
        viewModelScope.launch {
            _getBookingByTitleResponse.value = bookingUseCase.getBookingByTitle(title)
        }

    // PATCH - 모임 한 번 더 하기
    private val _patchBookingRestartResponse = MutableLiveData<Response<Unit>>()
    val patchBookingRestartResponse: LiveData<Response<Unit>> get() = _patchBookingRestartResponse
    fun patchBookingRestart(meetingId: Long) =
        viewModelScope.launch {
            _patchBookingRestartResponse.value = bookingUseCase.patchBookingRestart(meetingId)
        }

    // PATCH - 결제하기
    private val _patchPaymentResponse = MutableLiveData<Response<Unit>>()
    val patchPaymentResponse: LiveData<Response<Unit>> get() = _patchPaymentResponse
    fun patchPayment(meetingId: Long) =
        viewModelScope.launch {
            _patchPaymentResponse.value = bookingUseCase.patchPayment(meetingId)
        }
}
