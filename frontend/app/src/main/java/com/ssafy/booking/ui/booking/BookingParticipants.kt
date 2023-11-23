package com.ssafy.booking.ui.booking

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.MaterialTheme.colors
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.ssafy.booking.R
import com.ssafy.booking.di.App
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.domain.model.booking.BookingAcceptRequest
import com.ssafy.domain.model.booking.BookingDetail
import com.ssafy.domain.model.booking.BookingParticipants
import com.ssafy.domain.model.booking.BookingRejectRequest
import com.ssafy.domain.model.booking.BookingWaiting

@Composable
fun BookingParticipants(
    meetingId: Long,
    memberRole: String,
    meetingState: String,
) {
    // 뷰모델 연결
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val getParticipantsResponse by bookingViewModel.getParticipantsResponse.observeAsState()
    val getWaitingListResponse by bookingViewModel.getWaitingListResponse.observeAsState()
    var participantsList by remember { mutableStateOf<List<BookingParticipants>>(emptyList()) }
    var waitingList by remember { mutableStateOf<List<BookingWaiting>>(emptyList()) }

    LaunchedEffect(Unit) {
        bookingViewModel.getWaitingList(meetingId)
        bookingViewModel.getParticipants(meetingId)
    }
//
    // 참가자 바뀔 떄마다 업데이트
    LaunchedEffect(getParticipantsResponse, participantsList) {
        getParticipantsResponse?.body()?.let { response ->
            Log.d("참가대기자", "$response")
            var sortByParticipantsList = response.sortedBy { it.memberPk }
            participantsList = sortByParticipantsList // 상태 업데이트
        }
    }

    // 대기자 바뀔 때마다 업데이트
    LaunchedEffect(getWaitingListResponse, waitingList) {
        getWaitingListResponse?.body()?.let { response ->
            Log.d("참가대기자", "$response")
            var sortByWaitingList = response.sortedBy { it.memberPk }
            waitingList = sortByWaitingList // 상태 업데이트
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End,
        ) {
            Row {
                Text(text = "출석", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "결제", fontWeight = FontWeight.Bold)
            }
        }
//        Text(text = "참가자 목록", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(8.dp))
        participantsList.forEach { participant ->
            ParticipantItem(participant = participant)
            Spacer(modifier = Modifier.padding(8.dp))
        }

        Divider(modifier = Modifier.padding(16.dp))
        // 방장이면서 모임이 준비중일 때만 대기자 목록을 표시
        if (memberRole == "LEADER" && meetingState == "PREPARING") {
            Text(
                text = "대기자 목록",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
            waitingList.forEach { waiting ->
                WaitingListItem(
                    meetingId = meetingId,
                    waiting = waiting,
                    bookingViewModel = bookingViewModel
                )
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    }


}

// 참가자
@Composable
fun ParticipantItem(participant: BookingParticipants) {
    val navController = LocalNavigation.current
    val context = LocalContext.current
    val imageLoader = context.imageLoader
    val leaderId = App.prefs.getLeaderId()

    // 참가자 한 명에 대한 UI를 여기에 구성하세요.
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    navController.navigate("profile/${participant.memberPk}") {
                        popUpTo("login") { inclusive = true }
                    }
                }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("https://kr.object.ncloudstorage.com/booking-bucket/images/${participant.memberPk}_profile.png")
                    .memoryCachePolicy(CachePolicy.DISABLED)
                    .addHeader("Host", "kr.object.ncloudstorage.com")
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                imageLoader = imageLoader,
                error = painterResource(id = R.drawable.basic_profile),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = participant.nickname,
                fontWeight = FontWeight.Thin,
            )
            if (leaderId == participant.memberPk) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Leader",
                    tint = Color(0xFFFDFD96)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (participant.attendanceStatus) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_check_box_24),
                    contentDescription = "출석",
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_check_box_outline_blank_24),
                    contentDescription = "미출석",
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            if (participant.paymentStatus) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_check_box_24),
                    contentDescription = "결제 완료",
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_check_box_outline_blank_24),
                    contentDescription = "미결제",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

    }
}

// 대기자

@Composable
fun WaitingListItem(waiting: BookingWaiting, meetingId: Long, bookingViewModel: BookingViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween, // 아이템들을 양 끝으로 정렬
        modifier = Modifier.fillMaxWidth() // Row의 너비를 부모에 맞춤
    ) {
        val navController = LocalNavigation.current
        val context = LocalContext.current
        val imageLoader = context.imageLoader

        // 프로필 이미지와 닉네임을 포함하는 섹션
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    navController.navigate("profile/${waiting.memberPk}") {
                        popUpTo("login") { inclusive = true }
                    }
                }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("https://kr.object.ncloudstorage.com/booking-bucket/images/${waiting.memberPk}_profile.png")
                    .memoryCachePolicy(CachePolicy.DISABLED)
                    .addHeader("Host", "kr.object.ncloudstorage.com")
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                imageLoader = imageLoader,
                error = painterResource(id = R.drawable.basic_profile),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = waiting.nickname,
                fontWeight = FontWeight.Thin,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        // 버튼들을 포함하는 섹션
        Row {
            // 체크 버튼
            IconButton(
                onClick = {
                    val request =
                        BookingAcceptRequest(meetingId = meetingId, memberId = waiting.memberPk)
                    bookingViewModel.postBookingAccept(meetingId, waiting.memberPk, request)
                    // 다시 쏴서 리스트 갱신해
                    bookingViewModel.getParticipants(meetingId)
                    bookingViewModel.getWaitingList(meetingId)
                }, // 여기서는 대기자의 ID를 파라미터로 전달
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_check_circle_24),
                    contentDescription = "Approve",
                    tint = Color(0xFF00C68E),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp)) // 버튼 사이의 간격 추가
            // 엑스 버튼
            IconButton(
                onClick = {
                    val request =
                        BookingRejectRequest(meetingId = meetingId, memberId = waiting.memberPk)
                    bookingViewModel.postBookingReject(meetingId, waiting.memberPk, request)
                    // 다시 쏴서 리스트 갱신해
                    bookingViewModel.getParticipants(meetingId)
                    bookingViewModel.getWaitingList(meetingId)
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_cancel_24),
                    contentDescription = "Reject",
                    tint = Color(0xFFFF3971),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}