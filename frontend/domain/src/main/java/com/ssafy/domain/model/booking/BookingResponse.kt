package com.ssafy.domain.model.booking

import com.google.gson.annotations.SerializedName
import java.io.Serial
import java.time.LocalDate
import java.time.LocalDateTime


// 해시 태그를 위한 데이터 타입
data class HashtagResponse(
    @SerializedName("hashtagId")
    val hashtagId: Long,
    @SerializedName("content")
    val content: String
)

// 모임 목록 전체 조회
data class BookingAll (
    @SerializedName("meetingId")
    val meetingId : Long,
    @SerializedName("bookIsbn")
    val bookIsbn : String,
    @SerializedName("bookTitle")
    val bookTitle : String,
    @SerializedName("coverImage")
    val coverImage : String,
    @SerializedName("meetingTitle")
    val meetingTitle : String,
    @SerializedName("curParticipants")
    val curParticipants : Int,
    @SerializedName("maxParticipants")
    val maxParticipants : Int,
    @SerializedName("lat")
    val lat : Double,
    @SerializedName("lgt")
    val lgt : Double,
    @SerializedName("hashtagList")
    val hashtagList : List<HashtagResponse>,
    @SerializedName("address")
    val address : String,
)


data class ParticipantResponse(
    @SerializedName("memberPk")
    val memberPk: Int,
    @SerializedName("loginId")
    val loginId: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImage")
    val profileImage: String?,
    @SerializedName("attendanceStatus")
    val attendanceStatus: Boolean,
    @SerializedName("paymentStatus")
    val paymentStatus: Boolean

)

data class MeetingInfoResponse(
    @SerializedName("date")
    val date : String,
    @SerializedName("location")
    val location : String,
    @SerializedName("address")
    val address : String,
    @SerializedName("lat")
    val lat : Double,
    @SerializedName("lgt")
    val lgt : Double,
    @SerializedName("fee")
    val fee : Int,
    @SerializedName("meetinginfoId")
    val meetinginfoId : Long,
    )


// 모임 상세 조회
data class BookingDetail (
    @SerializedName("meetingId")
    val meetingId : Long,
    @SerializedName("leaderId")
    val leaderId : Int,
    @SerializedName("bookIsbn")
    val bookIsbn : String,
    @SerializedName("bookTitle")
    val bookTitle : String,
    @SerializedName("bookAuthor")
    val bookAuthor : String,
    @SerializedName("bookContent")
    val bookContent : String,
    @SerializedName("coverImage")
    val coverImage : String,
    @SerializedName("meetingTitle")
    val meetingTitle : String,
    @SerializedName("description")
    val description : String,
    @SerializedName("lat")
    val lat : Double,
    @SerializedName("lgt")
    val lgt : Double,
    @SerializedName("curParticipants")
    val curParticipants : Int,
    @SerializedName("maxParticipants")
    val maxParticipants : Int,
    @SerializedName("meetingState")
    val meetingState : String,
    @SerializedName("hashtagList")
    val hashtagList : List<HashtagResponse>,
    @SerializedName("meetingInfoList")
    val meetingInfoList : List<MeetingInfoResponse>,

)

// 모임 참여자 목록
data class BookingParticipants (
    @SerializedName("memberPk")
    val memberPk : Int,
    @SerializedName("loginId")
    val loginId : String,
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("profileImage")
    val profileImage : String?,

    @SerializedName("attendanceStatus")
    val attendanceStatus : Boolean,
    @SerializedName("paymentStatus")
    val paymentStatus : Boolean,

)
// 모임 대기자 목록

data class BookingWaiting (
    @SerializedName("loginId")
    val loginId : String,
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("profileImage")
    val profileImage : String,
    @SerializedName("memberPk")
    val memberPk : Int
)

// 해시태그 기반 모임 조회
data class BookingListByHashtag (
    @SerializedName("meetingId")
    val meetingId : Long,
    @SerializedName("bookIsbn")
    val bookIsbn : String,
    @SerializedName("bookTitle")
    val bookTitle : String,
    @SerializedName("coverImage")
    val coverImage : String,
    @SerializedName("meetingTitle")
    val meetingTitle : String,
    @SerializedName("curParticipants")
    val curParticipants : Int,
    @SerializedName("maxParticipants")
    val maxParticipants : Int,
    @SerializedName("lat")
    val lat : Double,
    @SerializedName("lgt")
    val lgt : Double,
    @SerializedName("meetingState")
    val meetingState : String,
    @SerializedName("hashtagList")
    val hashtagList : List<HashtagResponse>,
    @SerializedName("meetingInfoList")
    val meetingInfoList : List<MeetingInfoResponse>,
    @SerializedName("address")
    val address : String,

)

data class BookingListByMemberPk (
    @SerializedName("meetingId")
    val meetingId : Long,
    @SerializedName("bookIsbn")
    val bookIsbn : String,
    @SerializedName("bookTitle")
    val bookTitle : String,
    @SerializedName("coverImage")
    val coverImage : String,
    @SerializedName("meetingTitle")
    val meetingTitle : String,
    @SerializedName("curParticipants")
    val curParticipants : Int,
    @SerializedName("maxParticipants")
    val maxParticipants : Int,
    @SerializedName("lat")
    val lat : Double,
    @SerializedName("lgt")
    val lgt : Double,
    @SerializedName("meetingState")
    val meetingState : String,
    @SerializedName("hashtagList")
    val hashtagList : List<HashtagResponse>,
    @SerializedName("meetingInfoList")
    val meetingInfoList : List<MeetingInfoResponse>,
    @SerializedName("address")
    val address : String,
)

data class BookingListByTitle(
    @SerializedName("meetingId")
    val meetingId : Long,
    @SerializedName("bookIsbn")
    val bookIsbn : String,
    @SerializedName("bookTitle")
    val bookTitle : String,
    @SerializedName("coverImage")
    val coverImage : String,
    @SerializedName("meetingTitle")
    val meetingTitle : String,
    @SerializedName("curParticipants")
    val curParticipants : Int,
    @SerializedName("maxParticipants")
    val maxParticipants : Int,
    @SerializedName("lat")
    val lat : Double,
    @SerializedName("lgt")
    val lgt : Double,
    @SerializedName("meetingState")
    val meetingState : String,
    @SerializedName("hashtagList")
    val hashtagList : List<HashtagResponse>,
    @SerializedName("meetingInfoList")
    val meetingInfoList : List<MeetingInfoResponse>,
    @SerializedName("address")
    val address : String,
)

// 네이버 검색 api
data class SearchResponse(
    val items : List<SearchItem>
)

data class SearchItem(
    val title : String,
    val link : String,
    val category : String,
    val description : String,
    val telephone : String,
    val address : String,
    val roadAddress : String,
    val mapx : String,
    val mapy : String
)