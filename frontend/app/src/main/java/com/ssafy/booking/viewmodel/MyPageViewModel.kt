package com.ssafy.booking.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.booking.model.UserInfoChangeResult
import com.ssafy.booking.model.UserProfileState
import com.ssafy.booking.ui.profile.ProfileData
import com.ssafy.domain.model.mypage.AddressnModifyRequest
import com.ssafy.domain.model.mypage.UserFollowersResponse
import com.ssafy.domain.model.mypage.UserFollowingsResponse
import com.ssafy.domain.model.mypage.UserInfoResponse
import com.ssafy.domain.model.mypage.UserInfoResponseByNickname
import com.ssafy.domain.model.mypage.UserInfoResponseByPk
import com.ssafy.domain.model.mypage.UserModifyRequest
import com.ssafy.domain.usecase.MyBookUseCase
import com.ssafy.domain.usecase.MyPageUseCase
import com.ssafy.domain.usecase.NaverCloudUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myPageUseCase: MyPageUseCase,
    private val myBookUseCase: MyBookUseCase,
    private val naverCloudUseCase: NaverCloudUseCase
) : ViewModel() {

    // 마이페이지 에러 핸들링
    private val _profileState = MutableLiveData<UserProfileState>()
    val profileState: LiveData<UserProfileState> = _profileState

    // GET - 유저 정보 요청 로직
    private val _getUserInfoResponse = MutableLiveData<Response<UserInfoResponse>>()
    val getUserInfoResponse: LiveData<Response<UserInfoResponse>> get() = _getUserInfoResponse

    fun getUserInfo(loginId: String) =
        viewModelScope.launch {
            _getUserInfoResponse.value = myPageUseCase.getUserInfo(loginId)
        }

    // PATCH - 회원 정보 수정 요청 로직
    private val _patchUserInfoResponse = MutableLiveData<Response<Unit>>()
    val patchUserInfoResponse: LiveData<Response<Unit>> get() = _patchUserInfoResponse

    // StateFlow를 사용하여 UI 계층에 상태 전달
    private val _userInfoChangeResult = MutableStateFlow<UserInfoChangeResult?>(null)
    val userInfoChangeResult: StateFlow<UserInfoChangeResult?> = _userInfoChangeResult.asStateFlow()

    fun userInfoChange(nick: String, loginId: String, memberPk: Long, requestBody: RequestBody?) {
        // pImg 는 파일 이름으로 memberPk_profile.png 형식으로 들어갈 예정.
        val requestInfo = UserModifyRequest(loginId = loginId, nickname = nick, profileImage = "${memberPk}_profile.png")
        Log.d("requestInfo", "$requestInfo")

        viewModelScope.launch {
            // requestBody = inputStream data 를 뜻함.
            if(requestBody != null) {
                naverCloudUseCase.putObject("booking-bucket", "images/${memberPk}_profile.png", requestBody)
            }
            myPageUseCase.patchUserInfo(requestInfo).collect { response ->
                Log.d("requestInfo", "$response")
                if (response.isSuccessful) {
                    // 성공 상태를 StateFlow에 업데이트
                    _userInfoChangeResult.value = UserInfoChangeResult.Success(nick, "${memberPk}_profile.png", "profile/$memberPk")
                } else {
                    // 실패 상태를 StateFlow에 업데이트
                    _userInfoChangeResult.value = UserInfoChangeResult.Error(true)
                }
            }
        }
    }

    // naverCloud upload put 요청
    private val _naverCloudPutResponse = MutableLiveData<Response<Unit>>()
    val naverCloudPutResponse : LiveData<Response<Unit>> get() = _naverCloudPutResponse

    fun uploadImageToNaverCloud(requestBody: RequestBody, memberPK: Long) {
        viewModelScope.launch {
            _naverCloudPutResponse.value = naverCloudUseCase.putObject("booking-bucket", "images/${memberPK}_profile.png", requestBody)
        }
    }

    // naverCloud get 요청 불러오기
    private val _naverCloudGetResponse = MutableLiveData<Response<ResponseBody>>()
    val naverCloudGetResponse : LiveData<Response<ResponseBody>> get() = _naverCloudGetResponse

    fun GetToNaverCloud(memberPk: Long) =
        viewModelScope.launch {
            _naverCloudGetResponse.value = naverCloudUseCase.getObject("booking-bucket", "images/${memberPk}_profile.png")
        }

    // PATCH - 회원 위치 정보 수정 요청 로직
    private val _patchUserAddressResponse = MutableLiveData<Response<Unit>>()
    val patchUserAddressResponse: LiveData<Response<Unit>> get() = _patchUserAddressResponse

    fun patchUserAddress(request: AddressnModifyRequest) =
        viewModelScope.launch {
            _patchUserAddressResponse.value = myPageUseCase.patchUserAddress(request)
        }

    // POST - 회원 탈퇴 요청 로직
    private val _postUserDeleteResponse = MutableLiveData<Response<Unit>>()
    val postUserDeleteResponse: LiveData<Response<Unit>> get() = _postUserDeleteResponse

    fun postUserDelete(loginId: String) =
        viewModelScope.launch {
            _postUserDeleteResponse.value = myPageUseCase.deleteUser(loginId)
        }

    // GET - 팔로워 수 요청 로직
    private val _getUserFollowersResponse = MutableLiveData<Response<UserFollowersResponse>>()
    val getUserFollowersResponse: LiveData<Response<UserFollowersResponse>> get() = _getUserFollowersResponse

    fun getUserFollowers(memberPk: Long) =
        viewModelScope.launch {
            _getUserFollowersResponse.value = myPageUseCase.getUserFollowers(memberPk)
        }

    // 팔로우, 언팔로우 확인
    private var _isFollowNow = MutableLiveData<Boolean>(null)
    val isFollowNow : LiveData<Boolean> get() = _isFollowNow

    fun checkFollowNow(memberPk: Long, profileData:ProfileData) {
        profileData.followers?.followers?.let {followerList->
            followerList.forEach{follower->
                if (follower?.memberPk == memberPk) {
                    _isFollowNow.value = true
                }
            }
        }
    }

    // GET - 팔로잉 수 요청 로직
    private val _getUserFollowingsResponse = MutableLiveData<Response<UserFollowingsResponse>>()
    val getUserFollowingsResponse: LiveData<Response<UserFollowingsResponse>> get() = _getUserFollowingsResponse

    fun getUserFollowings(memberPk: Long) =
        viewModelScope.launch {
            _getUserFollowingsResponse.value = myPageUseCase.getUserFollowings(memberPk)
        }

    // GET - memberPk 로 유저정보 가져오기
    private val _getUserInfoResponseByPk = MutableLiveData<Response<UserInfoResponseByPk>>()
    val getUserInfoResponseByPk: LiveData<Response<UserInfoResponseByPk>> get() = _getUserInfoResponseByPk

    fun getUserInfoResponseByPk(memberPk: Long) =
        viewModelScope.launch {
            _getUserInfoResponseByPk.value = myPageUseCase.getUserInfoByPk(memberPk)
        }

    // GET - nickname 으로 유저정보 가져오기
    private val _getUserInfoResponseByNickname = MutableLiveData<Response<UserInfoResponseByNickname>>()
    val getUserInfoResponseByNickname: LiveData<Response<UserInfoResponseByNickname>> get() = _getUserInfoResponseByNickname

    fun getUserInfoResponseByNickname(nickname: String) =
        viewModelScope.launch {
            _getUserInfoResponseByNickname.value = myPageUseCase.getUserInfoByNickname(nickname)
        }

    // POST - follow 요청하기
    private val _postFollow = MutableLiveData<Response<Unit>>()
    val postFollow: LiveData<Response<Unit>> get() = _postFollow

    fun postFollow(memberPk: Long) =
        viewModelScope.launch {
            _postFollow.value = myPageUseCase.postFollow(memberPk)
            _isFollowNow.value = true
        }

    // DELETE - unfollow 요청하기
    private val _deleteFollow = MutableLiveData<Response<Unit>>()
    val deleteFollow: LiveData<Response<Unit>> get() = _deleteFollow

    fun deleteFollow(memberPk: Long) =
        viewModelScope.launch {
            _deleteFollow.value = myPageUseCase.deleteFollow(memberPk)
            _isFollowNow.value = false
        }

    // 최종 마이페이지 작업 로직
    fun getMyPage(memberPk: Long, yourMemberPk: Long) = viewModelScope.launch {
        _profileState.value = UserProfileState.Loading
        // 이쪽이 memberPk url 로 접근한 값이랑 기존 내 sharedPreference 에 있는 memberPk 값이 일치 불일치로 true/false 반호나


        // yourMemberPk, memberPk 일치 할때 불일치 할때 => 인자로 받을거임
        var userInfoResponse = myPageUseCase.getUserInfoByPk(yourMemberPk)
        val userFollowersResponse = myPageUseCase.getUserFollowers(yourMemberPk)
        val userFollowingsResponse = myPageUseCase.getUserFollowings(yourMemberPk)
        val userBookResponse = myBookUseCase.getBookList(yourMemberPk)

        if (userFollowersResponse.isSuccessful && userFollowingsResponse.isSuccessful && userInfoResponse.isSuccessful && userBookResponse.isSuccessful &&
            userFollowersResponse.body() != null && userFollowingsResponse.body() != null && userInfoResponse.body() != null && userBookResponse.body() != null) {

            if (yourMemberPk == memberPk) {
                // 일치 할떄 => 마이페이지로 접근 로직
                val profileData = ProfileData(
                    myProfile = userInfoResponse.body(),
                    followers = userFollowersResponse.body(),
                    followings = userFollowingsResponse.body(),
                    readBook = userBookResponse.body(),
                    isI = true
                    // readBookNumber는 예시에 없으므로 기본값 0을 사용
                )
                _profileState.value = UserProfileState.Success(profileData)
            } else {
                // 불일치 할때 => 너의 페이지로 접근 로직
                val profileData = ProfileData(
                    myProfile = userInfoResponse.body(),
                    followers = userFollowersResponse.body(),
                    followings = userFollowingsResponse.body(),
                    readBook = userBookResponse.body(),
                    isI = false
                    // readBookNumber는 예시에 없으므로 기본값 0을 사용
                )
                _profileState.value = UserProfileState.Success(profileData)
            }
        } else {
            _profileState.value = UserProfileState.Error("Failed to fetch user info.")
        }


//        // 추후 유저 팔로우 조회
//
//        try {
//            val userInfoResponse = myPageUseCase.getUserInfo(loginId)
//            if (userInfoResponse.isSuccessful && userInfoResponse.body() != null) {
//                // loginId 를 통한 유저 정보 조회 완료 시 - 팔로워 팔로잉 목록 조회
//                userInfoResponse.body()?.memberPk?.let { memberPk ->
//                    val userFollowersResponse = myPageUseCase.getUserFollowers(memberPk)
//                    val userFollowingsResponse = myPageUseCase.getUserFollowings(memberPk)
//
//                    if (userFollowersResponse.isSuccessful && userFollowingsResponse.isSuccessful &&
//                        userFollowersResponse.body() != null && userFollowingsResponse.body() != null
//                    ) {
//                        val profileData = ProfileData(
//                            myProfile = userInfoResponse.body(),
//                            followers = userFollowersResponse.body(),
//                            followings = userFollowingsResponse.body(),
//                            readBook = null,
//                            isI = true
//                            // readBookNumber는 예시에 없으므로 기본값 0을 사용
//                        )
//                        _profileState.value = UserProfileState.Success(profileData)
//                    } else {
//                        _profileState.value = UserProfileState.Error("Failed to fetch followers && followings.")
//                        return@launch
//                    }
//                } ?: run {
//                    _profileState.value = UserProfileState.Error("nickname is null")
//                }
//            } else {
//                _profileState.value = UserProfileState.Error("Failed to fetch user info.")
//            }
//        } catch (e: Exception) {
//            _profileState.value = UserProfileState.Error(e.message ?: "Unknown error occurred")
//        }
    }

    val combinedUserFollowData = MediatorLiveData<Pair<UserFollowersResponse?, UserFollowingsResponse?>>().apply {
        var followersResponse: UserFollowersResponse? = null
        var followingsResponse: UserFollowingsResponse? = null

        fun update() {
            if (followersResponse != null && followingsResponse != null) {
                this.value = Pair(followersResponse, followingsResponse)
            }
        }

        addSource(getUserFollowersResponse) { response ->
            followersResponse = response.body()
            update()
        }

        addSource(getUserFollowingsResponse) { response ->
            followingsResponse = response.body()
            update()
        }
    }
}
