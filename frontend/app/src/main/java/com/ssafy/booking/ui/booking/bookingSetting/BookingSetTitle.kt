package com.ssafy.booking.ui.booking.bookingSetting

import ParticipantCounter
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Text
import coil.compose.rememberImagePainter
import com.ssafy.booking.di.App
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.BackTopBar
import com.ssafy.booking.ui.common.TopBar
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.domain.model.booking.BookingModifyRequest
import com.ssafy.domain.model.booking.HashtagResponse

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetTitle() {
    val viewModel: BookingViewModel = hiltViewModel()
    val bookingDetailResponse by viewModel.getBookingDetailResponse.observeAsState()
    val title = App.prefs.getTitle()
    val description = App.prefs.getDescription()
    val maxParticipants = App.prefs.getMaxParticipants()
    val bookImage = App.prefs.getBookImage()
    val bookTitle = App.prefs.getBookTitle()
    val bookAuthor = App.prefs.getBookAuthor()
    val titleState by viewModel.title.observeAsState()
    val descriptionState by viewModel.description.observeAsState()
    val maxParticipantsState by viewModel.maxParticipants.observeAsState()


    LaunchedEffect(Unit) {
        viewModel.title.value = title
        viewModel.description.value = description
        viewModel.maxParticipants.value = maxParticipants
    }

    Scaffold(
        topBar = {
            BackTopBar(title = "수정하기")
        },
        bottomBar = {
            SetTitleBottomButton(titleState,descriptionState,maxParticipantsState,viewModel) // 하단 버튼
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // 책 이미지
            bookImage?.let {
                Column{
                    Text(text = bookTitle ?: "")
                    Text(text = bookAuthor ?: "")
                    Image(
                        painter = rememberImagePainter(bookImage),
                        contentDescription = "책 이미지",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }

            // 제목 입력 필드
            title?.let {
                OutlinedTextField(
                    singleLine = true,
                    value = titleState ?: "",
                    onValueChange = { newValue ->
                        viewModel.title.value = newValue
                    },
                    label = { Text("제목", color = Color.Black) },
                    modifier = Modifier
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 설명 입력 필드
            description?.let {
                OutlinedTextField(
                    maxLines = 10,
                    value = descriptionState ?: "",
                    onValueChange = { newValue ->
                        viewModel.description.value = newValue
                    },
                    label = { Text("모임 소개", color = Color.Black) },
                    modifier = Modifier
                        .padding(16.dp)
                        .height(IntrinsicSize.Min)
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))
            // 참가자 수 입력 필드
            maxParticipants?.let {
                    ParticipantCounter(
                        maxParticipants = maxParticipantsState ?: 0,
                        onMaxParticipantsChanged = { newCount -> viewModel.maxParticipants.value = newCount }
                    )
                }
        }
    }
}

@Composable
fun SetTitleBottomButton(
    titleState: String?,
    descriptionState: String?,
    maxParticipantsState: Int?,
    viewModel: BookingViewModel
) {
    // 현재 Composable 함수와 연관된 Context 가져오기
    val context = LocalContext.current
    val navController = LocalNavigation.current
    val patchBookingDetailResponse by viewModel.patchBookingDetailResponse.observeAsState()
    val meetingId = App.prefs.getMeetingId()
    val hashtagList = App.prefs.getHashtagList()
    val contentList = hashtagList?.map { it.content }?: listOf("")


    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                if (titleState.isNullOrEmpty() || descriptionState.isNullOrEmpty() || maxParticipantsState == 0) {
                    // 제목 또는 내용이 비어있을 경우 Toast 메시지 표시
                    Toast.makeText(context, "제목과 내용, 참가인원을 모두 입력해주세요.", Toast.LENGTH_LONG).show()
                } else {
                    // 모두 입력된 경우 네비게이션
                    val request = BookingModifyRequest(
                        meetingId = meetingId!!,
                        meetingTitle = titleState,
                        description = descriptionState,
                        maxParticipants = maxParticipantsState!!,
                        hashtagList = contentList
                    )
                    viewModel.patchBookingDetail(request)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(3.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFf00C68E))
        ) {
            Text("수정 완료", style = MaterialTheme.typography.bodyMedium)
        }
        LaunchedEffect(patchBookingDetailResponse) {
            patchBookingDetailResponse?.let {
                if (it.isSuccessful) {
                    // 성공적인 응답일 경우 네비게이션 진행
                    navController.navigate("bookingDetail/${meetingId}") {
                        popUpTo("booking/setting/title") { inclusive = true }
                        launchSingleTop = true
                    }
                } else {
                    // 실패했을 경우 에러 처리
                    Toast.makeText(context, "수정이 정상적으로 되지 않았습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


