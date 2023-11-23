package com.ssafy.data.repository

import com.ssafy.data.remote.api.MyPageApi
import com.ssafy.domain.model.mypage.AddressnModifyRequest
import com.ssafy.domain.model.mypage.UserFollowersResponse
import com.ssafy.domain.model.mypage.UserFollowingsResponse
import com.ssafy.domain.model.mypage.UserInfoResponse
import com.ssafy.domain.model.mypage.UserInfoResponseByNickname
import com.ssafy.domain.model.mypage.UserInfoResponseByPk
import com.ssafy.domain.model.mypage.UserModifyRequest
import com.ssafy.domain.repository.MyPageRepository
import retrofit2.Response
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageApi: MyPageApi
) :MyPageRepository {
    override suspend fun getUserInfo(loginId : String) : Response<UserInfoResponse> {
        return myPageApi.getUserInfo(loginId)
    }
    override suspend fun patchUserInfo(request: UserModifyRequest) : Response<Unit> {
        return myPageApi.patchUserInfo(request)
    }
    override suspend fun patchUserAddress(request: AddressnModifyRequest) : Response<Unit> {
        return myPageApi.patchUserAddress(request)
    }
    override suspend fun deleteUser(loginId: String) : Response<Unit> {
        return myPageApi.deleteUser(loginId)
    }
    override suspend fun getUserFollowers(memberPk : Long) : Response<UserFollowersResponse> {
        return myPageApi.getUserFollowers(memberPk)
    }
    override suspend fun getUserFollowings(memberPk : Long) : Response<UserFollowingsResponse> {
        return myPageApi.getUserFollowings(memberPk)
    }

    override suspend fun getUserInfoByPk(memberPk: Long): Response<UserInfoResponseByPk> {
        return myPageApi.getUserInfoByPk(memberPk)
    }

    override suspend fun getUserInfoByNickname(nickname: String): Response<UserInfoResponseByNickname> {
        return myPageApi.getUserInfoByNickname(nickname)
    }

    override suspend fun postFollow(memberPk: Long): Response<Unit> {
        return myPageApi.postFollow(memberPk)
    }

    override suspend fun deleteFollow(memberPk: Long): Response<Unit> {
        return myPageApi.deleteFollow(memberPk)
    }

}