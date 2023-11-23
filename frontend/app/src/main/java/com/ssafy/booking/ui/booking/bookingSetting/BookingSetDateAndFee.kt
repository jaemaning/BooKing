package com.ssafy.booking.ui.booking.bookingSetting

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.booking.R
import com.ssafy.booking.di.App
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.BackTopBar
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.domain.model.booking.BookingStartRequest
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SetDateAndFee() {
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val dateState by bookingViewModel.date.observeAsState()
    val timeState by bookingViewModel.time.observeAsState()
    val feeState by bookingViewModel.fee.observeAsState()
    val placeNameState by bookingViewModel.placeName.observeAsState()
    val locationState by bookingViewModel.location.observeAsState()

    Scaffold(
        topBar = {
            BackTopBar(title = "모임 날짜&참가비 선택")
        },
        bottomBar = {
            if (dateState != null && timeState != null && feeState != null) {
                // 바텀 버튼을 Scaffold의 bottomBar로 설정합니다.
                SetDateAndFeeBottomButton(dateState!!, timeState!!, feeState, bookingViewModel)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 8.dp),
        ) {
            Text(text = "날짜", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                DatePickerComposable(onDateSelected = { date ->
                    bookingViewModel.date.value = date
                })
                Spacer(modifier = Modifier.padding(4.dp))
                Text("${dateState ?: "날짜를 선택해주세요."}", fontSize = 20.sp)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimePickerComposable(onTimeSelected = { time ->
                    bookingViewModel.time.value = time
                })
                Spacer(modifier = Modifier.padding(4.dp))
                Text("${timeState ?: "시간을 선택해주세요."}", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.padding(24.dp))
            Divider()
            Text(
                text = "참가비",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
            SetEntryFee(bookingViewModel)
        }
    }
}

@Composable
fun DatePickerComposable(onDateSelected: (LocalDate) -> Unit) {
    val context = LocalContext.current
    val currentDate = LocalDate.now()
    val datePickerDialog = remember {
        DatePickerDialog(context, { _, year, month, dayOfMonth ->
            onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
        }, currentDate.year, currentDate.monthValue - 1, currentDate.dayOfMonth).apply {
            // 오늘과 오늘 이후만 선택가능하게 예외처리
            datePicker.minDate = System.currentTimeMillis() - 1000
        }
    }
    IconButton(
        onClick = { datePickerDialog.show() },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_calendar_month_24),
            contentDescription = "시간 선택",
            tint = Color(0xFF00C68E)
        )
    }
}

@Composable
fun TimePickerComposable(onTimeSelected: (LocalTime) -> Unit) {
    val context = LocalContext.current
    val timePickerDialog = remember {
        TimePickerDialog(context, { _, hour, minute ->
            onTimeSelected(LocalTime.of(hour, minute))
        }, LocalTime.now().hour, LocalTime.now().minute, true)
    }
    IconButton(
        onClick = { timePickerDialog.show() },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_access_time_24),
            contentDescription = "시간 선택",
            tint = Color(0xFF00C68E)
        )
    }
}

@Composable
fun SetEntryFee(bookingViewModel: BookingViewModel,modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        FeeInputField(bookingViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeeInputField(bookingViewModel: BookingViewModel) {
    var fee by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        OutlinedTextField(
            value = fee.toString(),
            onValueChange = { newValue ->
                fee = newValue.toIntOrNull() ?: 0
                bookingViewModel.fee.value = fee

                Log.d("참가비",bookingViewModel.fee.value.toString())
            },
            label = { Text("참가비 입력") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = { Icon(Icons.Filled.Add, contentDescription = "Money Icon") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { fee += 100
                    bookingViewModel.fee.value = fee},
                colors = ButtonDefaults.buttonColors(Color(0xFf00C68E))
            ) { Text("+1백", fontSize = 16.sp) }
            Button(
                onClick = { fee += 1000
                    bookingViewModel.fee.value = fee
                          },
                colors = ButtonDefaults.buttonColors(Color(0xFf00C68E))
            ) { Text("+1천", fontSize = 16.sp) }
            Button(
                onClick = { fee += 5000
                    bookingViewModel.fee.value = fee
                    Log.d("참가비22",bookingViewModel.fee.value.toString()) },
                colors = ButtonDefaults.buttonColors(Color(0xFf00C68E))
            ) { Text("+5천", fontSize = 16.sp) }
            Button(
                onClick = { fee += 10000
                    bookingViewModel.fee.value = fee
                          },
                colors = ButtonDefaults.buttonColors(Color(0xFf00C68E))
            ) { Text("+1만", fontSize = 16.sp) }
        }

    }
}

@Composable
fun SetDateAndFeeBottomButton(
    dateState: LocalDate,
    timeState: LocalTime,
    feeState: Int?,
    bookingViewModel: BookingViewModel
) {
    // 현재 Composable 함수와 연관된 Context 가져오기
    val context = LocalContext.current
    val navController = LocalNavigation.current

    val bookingStartRequestResponse by bookingViewModel.postBookingStartResponse.observeAsState()
    LaunchedEffect(bookingStartRequestResponse) {
        bookingStartRequestResponse?.let { response ->
            if (response.isSuccessful) { // Assuming 'isSuccessful' is a flag in your response indicating success
                val meetingId = App.prefs.getMeetingId()
                navController.navigate("bookingDetail/$meetingId") {
                    popUpTo("booking/setting/location") { inclusive = true }
                    launchSingleTop = true
                }
            } else {
                Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                val date =
                    LocalDateTime.of(bookingViewModel.date.value, bookingViewModel.time.value)
                        .withSecond(0)
                Log.d("date", date.toString())
                Log.d("date1", feeState.toString())
                Log.d("date", dateState.toString())
                Log.d("date", timeState.toString())
                if (date == null || feeState == null) {
                    // 제목 또는 내용이 비어있을 경우 Toast 메시지 표시
                    Toast.makeText(context, "독서모임의 모임 일정과 참가비를 모두 입력해주세요.", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Log.d("date123", date.toString())
                    val request = BookingStartRequest(
                        meetingId = App.prefs.getMeetingId()!!,
                        date = date.toString(),
                        fee = bookingViewModel.fee.value?:0,
                        lat = App.prefs.getMeetingLat()!!.toDouble(),
                        lgt = App.prefs.getMeetingLgt()!!.toDouble(),
                        address = App.prefs.getMeetingAddress()!!,
                        location = App.prefs.getMeetingLocation()!!,
                    )
                    bookingViewModel.postBookingStart(request)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(3.dp)
        ) {
            Text(text = "모임 확정",fontWeight=FontWeight.Bold)
        }
    }
}