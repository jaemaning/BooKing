package com.ssafy.booking.ui.booking

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Indicator
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.ssafy.booking.R
import com.ssafy.booking.di.App
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.domain.model.booking.BookingDetail
import com.ssafy.domain.model.booking.HashtagResponse
import com.ssafy.domain.model.booking.MeetingInfoResponse
import io.grpc.Context
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun BookingInfo(
    meetingId: Long,
    memberRole: String,
    meetingState: String
) {
    // 뷰모델 연결
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val getBookingDetailResponse by bookingViewModel.getBookingDetailResponse.observeAsState()
    var bookingDetail by remember { mutableStateOf<BookingDetail?>(null) }
    var firstMeetinginfoId by remember { mutableStateOf(-1) }
    val onFirstMeetingIdChange = { newValue: Int ->
        firstMeetinginfoId = newValue
    }


    LaunchedEffect(Unit) {
        bookingViewModel.getBookingDetail(meetingId)

    }
    LaunchedEffect(getBookingDetailResponse) {
        getBookingDetailResponse?.body()?.let { response ->
            Log.d("test334", "$response")
            bookingDetail = response // 상태 업데이트
            bookingDetail?.let {
                App.prefs.putBookTitle(bookingDetail?.bookTitle)
                App.prefs.putDescription(bookingDetail?.description)
                App.prefs.putBookAuthor(bookingDetail?.bookAuthor)
                App.prefs.putBookImage(bookingDetail?.coverImage)
                App.prefs.putTitle(bookingDetail?.meetingTitle)
                App.prefs.putMaxParticipants(bookingDetail?.maxParticipants)
                App.prefs.putHashtagList(bookingDetail?.hashtagList)
                App.prefs.putLeaderId(bookingDetail?.leaderId)
            }
        }
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val navController = LocalNavigation.current
        // 이미지
        val imagePainter = if (bookingDetail?.coverImage != null) {
            rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = bookingDetail?.coverImage)
                    .apply(block = fun ImageRequest.Builder.() {
                        crossfade(true)
                    }).build()
            )
        } else {
            painterResource(id = R.drawable.main1) // 기본 이미지
        }

        Image(
            painter = imagePainter,
            contentDescription = "Book Image",
            modifier = Modifier
                .size(120.dp, 150.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp)) // 이미지와 텍스트 사이 간격

        // 책 제목
        Text(
            text = "${bookingDetail?.bookTitle.orEmpty()}",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            // 글씨 크기 조정
        )

        // 책 작가
        Text(
            text = "${bookingDetail?.bookAuthor.orEmpty()}",
            fontSize = 12.sp
        )

        Divider(
            color = Color.LightGray,
            thickness = 0.8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 13.dp)
        )
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = bookingDetail?.meetingTitle.orEmpty(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ) // 모임 제목
                if (firstMeetinginfoId != -1) {
                    Text(
                        text = "녹음 보기",
                        modifier = Modifier
                            .clickable(onClick = {
                                navController.navigate("history/detail/${meetingId}/${firstMeetinginfoId}/${1}")
                            }),
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),

                ) {
                bookingDetail?.hashtagList?.forEach { hashtag ->
                    HashtagChip(tag = hashtag.content, id = hashtag.hashtagId) // 해시태그 칩 표시
                } ?: Text(text = "해시태그 없음")
            }
//            Text(text = "참가 인원 : ${bookingDetail?.curParticipants?: "정보없음"}명",fontSize = 14.sp) // 참가 인원
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = bookingDetail?.description.orEmpty(), fontSize = 14.sp) // 모임 설명
            Spacer(modifier = Modifier.height(4.dp))

        }
        MeetingInfoTimeline(
            bookingDetail = bookingDetail,
            meetingId,
            firstMeetinginfoId,
            onFirstMeetingIdChange
        ) // 모임 정보 타임라인
    }
}


@Composable
fun MeetingInfoCard(
    meetingInfo: MeetingInfoResponse,
    meetingId: Long,
    isFirstItem: Boolean,
    index: Int,
    firstMeetinginfoId: Int,
    onFirstMeetingIdChange: (Int) -> Unit
) {
    val navController = LocalNavigation.current
    val meetingDate = meetingInfo.date
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val dateTime = LocalDateTime.parse(meetingDate, formatter)

    val outputFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")
    val date = dateTime.format(outputFormatter)
    // MeetingInfoList의 처음과 나머지 구분.
    if (isFirstItem) {
        onFirstMeetingIdChange(meetingInfo.meetinginfoId.toInt())
        // 첫 번째 항목에만 표시할 추가 텍스트
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),

            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(text = "모임 일시", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(text = date ?: "아직 정해지지 않았습니다.", fontSize = 15.sp)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),

            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(text = "참가비", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(text = "${meetingInfo.fee}원", fontSize = 15.sp)

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(text = "모임 장소", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(text = meetingInfo.location, fontSize = 15.sp)
        }
        Map(meetingInfo = meetingInfo)
    } else {
        if (index == 1) {
            Divider(
                color = Color.LightGray,
                thickness = 0.8.dp,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "이전 모임", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    navController.navigate("history/detail/${meetingId}/${meetingInfo.meetinginfoId}/${index}")
                }),
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = meetingInfo.location, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "모임 일정", fontSize = 15.sp)
                    Text(text = date ?: "모임 일정이 아직 정해지지 않았습니다.")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "참가비")
                    Text(text = "${meetingInfo.fee}원", fontSize = 15.sp)
                }



            }
        }
    }
}

//@Composable
//fun MeetingInfoTimeline(
//    bookingDetail: BookingDetail?,
//    meetingId: Long
//) {
//    bookingDetail?.meetingInfoList?.let { meetingInfoList ->
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .fillMaxHeight()
//        ) {
//            itemsIndexed(meetingInfoList) { index, meetingInfo ->
//                // 첫 번째 항목인지 여부에 따라 MeetingInfoCard 호출
//                MeetingInfoCard(meetingInfo, meetingId, isFirstItem = index == 0)
//            }
//        }
//    } ?: Text(text = "아직 모임 정보가 없습니다.")
//}

@Composable
fun MeetingInfoTimeline(
    bookingDetail: BookingDetail?,
    meetingId: Long,
    firstMeetinginfoId: Int,
    onFirstMeetingIdChange: (Int) -> Unit
) {
    bookingDetail?.meetingInfoList?.let { meetingInfoList ->
        Column(
            modifier = Modifier
                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
        ) {
            meetingInfoList.forEachIndexed { index, meetingInfo ->
                MeetingInfoCard(
                    meetingInfo,
                    meetingId,
                    isFirstItem = index == 0,
                    meetingInfoList.size - index,
                    firstMeetinginfoId,
                    onFirstMeetingIdChange
                )
            }
        }
    } ?: Text(text = "아직 모임 정보가 없습니다.")
}


// 지도
//@OptIn(ExperimentalNaverMapApi::class)
@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun Map(meetingInfo: MeetingInfoResponse) {
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(maxZoom = 20.0, minZoom = 2.0)
        )
    }
    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(isLocationButtonEnabled = false)
        )
    }

    // meetingInfo의 위도, 경도를 사용하여 약속장소
    val currentLocation = LatLng(meetingInfo.lat, meetingInfo.lgt)
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 meetingInfo의 위치로 설정합니다.
        position = CameraPosition(currentLocation, 16.0)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp)

    ) {
        NaverMap(
            properties = mapProperties,
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState
        ) {
            // meetingInfo 위치에 마커 찍기
            Marker(
                state = MarkerState(position = currentLocation),
                captionText = meetingInfo.location
            )
        }
    }
}