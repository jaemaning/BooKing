package com.ssafy.domain.model.mypage

import com.google.gson.annotations.SerializedName
import java.io.Serial

data class UserInfoResponse(

    @SerializedName("loginId")
    val loginId : String,
    @SerializedName("email")
    val email : String,
    @SerializedName("age")
    val age : Int,
    @SerializedName("gender")
    val gender : String,
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("fullname")
    val fullname : String,
    @SerializedName("lat")
    val lat : Double,
    @SerializedName("lgt")
    val lgt : Double,
    @SerializedName("profileImage")
    val profileImage : String,
    @SerializedName("provider")
    val provider : String,
    @SerializedName("memberPk")
    val memberPk: Long
)

data class UserInfoResponseByPk (

    @SerializedName("loginId")
    val loginId : String,
    @SerializedName("email")
    val email : String,
    @SerializedName("age")
    val age : Int,
    @SerializedName("gender")
    val gender : String,
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("fullname")
    val fullname : String,
    @SerializedName("lat")
    val lat : Double,
    @SerializedName("lgt")
    val lgt : Double,
    @SerializedName("profileImage")
    val profileImage : String,
    @SerializedName("provider")
    val provider : String,
    @SerializedName("memberPk")
    val memberPk: Long,
    @SerializedName("point")
    val point: Long
)

data class UserInfoResponseByNickname(

    @SerializedName("loginId")
    val loginId : String,
    @SerializedName("email")
    val email : String,
    @SerializedName("age")
    val age : Int,
    @SerializedName("gender")
    val gender : String,
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("fullname")
    val fullname : String,
    @SerializedName("lat")
    val lat : Double,
    @SerializedName("lgt")
    val lgt : Double,
    @SerializedName("profileImage")
    val profileImage : String,
    @SerializedName("provider")
    val provider : String,
    @SerializedName("memberPk")
    val memberPk: Long,
    @SerializedName("point")
    val point: Long
)

data class FollowersList (

    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("memberPk")
    val memberPk: Long
)

data class UserFollowersResponse (

    @SerializedName("followers")
    val followers: List<FollowersList?>,
    @SerializedName("followersCnt")
    val followersCnt: Int
)

data class UserFollowingsResponse (

    @SerializedName("followings")
    val followings: List<FollowersList?>,
    @SerializedName("followingsCnt")
    val followingsCnt: Int
)
