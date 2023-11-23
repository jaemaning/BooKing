package com.ssafy.data.remote.api

import com.ssafy.domain.model.mypage.AddressnModifyRequest
import com.ssafy.domain.model.mypage.UserFollowersResponse
import com.ssafy.domain.model.mypage.UserFollowingsResponse
import com.ssafy.domain.model.mypage.UserInfoResponse
import com.ssafy.domain.model.mypage.UserInfoResponseByNickname
import com.ssafy.domain.model.mypage.UserInfoResponseByPk
import com.ssafy.domain.model.mypage.UserModifyRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface MyPageApi {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/members/memberInfo/{loginId}")
    suspend fun getUserInfo(@Path("loginId") loginId: String) : Response<UserInfoResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @PATCH("/api/members/modification")
    suspend fun patchUserInfo(@Body request: UserModifyRequest) : Response<Unit>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @PATCH("/api/members/location")
    suspend fun patchUserAddress(@Body request: AddressnModifyRequest) : Response<Unit>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @DELETE("/api/members/deletion/{loginId}")
    suspend fun deleteUser(@Path("loginId") loginId: String) : Response<Unit>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/follows/followers/{memberPk}")
    suspend fun getUserFollowers(@Path("memberPk") memberPk: Long) : Response<UserFollowersResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/follows/followings/{memberPk}")
    suspend fun getUserFollowings(@Path("memberPk") memberPk: Long) : Response<UserFollowingsResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/members/memberInfo-pk/{memberPk}")
    suspend fun getUserInfoByPk(@Path("memberPk") memberPk: Long) : Response<UserInfoResponseByPk>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/members/memberInfo-nick/{nickname}")
    suspend fun getUserInfoByNickname(@Path("nickname") nickname: String) : Response<UserInfoResponseByNickname>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/api/follows/{memberPk}")
    suspend fun postFollow(@Path("memberPk") memberPk: Long) : Response<Unit>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @DELETE("/api/follows/{memberPk}")
    suspend fun deleteFollow(@Path("memberPk") memberPk: Long) : Response<Unit>

}