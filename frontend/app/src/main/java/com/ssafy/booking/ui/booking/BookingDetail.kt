
package com.ssafy.booking.ui.booking

import android.annotation.SuppressLint
import android.content.Context
import android.health.connect.datatypes.ExerciseRoute
import android.location.Location
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.ssafy.booking.di.App
import com.ssafy.booking.model.ErrorResponse
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.BackTopBar
import com.ssafy.booking.ui.common.TabBar
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.domain.model.booking.BookingAttendRequest
import com.ssafy.domain.model.booking.BookingJoinRequest
import org.json.JSONObject

val tabTitles = listOf("모임 정보", "참가자", "게시판")


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetail(meetingId: Long) {
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val getBookingDetailResponse by bookingViewModel.getBookingDetailResponse.observeAsState()
    val getParticipantsResponse by bookingViewModel.getParticipantsResponse.observeAsState()
    val getWaitingListResponse by bookingViewModel.getWaitingListResponse.observeAsState()
    val createBookingSuccess by bookingViewModel.createBookingSuccess.observeAsState()
    val postBookingJoinResponse by bookingViewModel.postBookingJoinResponse.observeAsState()
    val context = LocalContext.current
    val navController = LocalNavigation.current
    // 리더인지 아닌지
    var memberRole by remember { mutableStateOf("GUEST") }
    // 모임 진행 상황
    var meetingState by remember { mutableStateOf("PREPAIRING") }
    LaunchedEffect(Unit) {
        bookingViewModel.getBookingDetail(meetingId)
        bookingViewModel.getParticipants(meetingId)
        bookingViewModel.getWaitingList(meetingId)
    }
    // 3개 중 하나라도 바뀌면 리렌더링
    LaunchedEffect(getBookingDetailResponse, getParticipantsResponse, getWaitingListResponse,createBookingSuccess,postBookingJoinResponse,bookingViewModel.getWaitingListResponse.value) {
        meetingState = getBookingDetailResponse?.body()?.meetingState ?: "PREPARING"
        // 리더인지 확인
        val memberPk = App.prefs.getMemberPk().toInt()
        val leaderId = getBookingDetailResponse?.body()?.leaderId
        if (memberPk == leaderId) {
            memberRole = "LEADER"
        } else {
            // 참여자 명단 확인
            val participants = getParticipantsResponse?.body()
            val isParticipant = participants?.any { it.memberPk == memberPk }

            // 대기자 명단 확인
            val waitingList = getWaitingListResponse?.body()
            val isWaiting = waitingList?.any { it.memberPk == memberPk }

            when {
                isParticipant == true -> memberRole = "PARTICIPANT" // 참여자
                isWaiting == true -> memberRole = "WAITING" // 대기자
                else -> memberRole = "GUEST" // 모두 해당되지 않으면 게스트
            }
        }
    }

    Scaffold(
        bottomBar = {
        BottomBar(
            memberRole,
            meetingState,
            meetingId,
            bookingViewModel,
            context,
            navController,
        ) }
    )

    { paddingValues ->
        LazyColumn(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            item {
                BackTopBar(title = "모임 상세")
            }
            item {
                TabBar(
                    tabTitles,
                    contentForTab = { index ->
                        when (index) {
                            0 -> BookingInfo(
                                meetingId = meetingId,
                                memberRole = memberRole,
                                meetingState = meetingState,
                            )

                            1 -> BookingParticipants(
                                meetingId = meetingId,
                                memberRole = memberRole,
                                meetingState = meetingState,
                            )

                            2 -> BookingBoard(
                                meetingId = meetingId,
                                memberRole = memberRole,
                                meetingState = meetingState,
                            )
                        }
                    }
                )
            }


            }
        }
    }



@Composable
fun BottomBar(memberRole:String,meetingState:String,meetingId:Long,bookingViewModel: BookingViewModel,context:Context ,navController:NavController){
    when (meetingState) {
        "PREPARING" -> preparingBottomBar(memberRole,meetingId,bookingViewModel,context,navController)
        "ONGOING" -> ongoingBottomBar(memberRole,meetingId,bookingViewModel,context,navController)
        "FINISH" -> finishBottomBar(memberRole,meetingId)
    }
}

// 상태가 PREPAIRING 일 때,
@Composable
fun preparingBottomBar(memberRole:String,meetingId:Long,bookingViewModel: BookingViewModel,context:Context ,navController:NavController) {
    when (memberRole) {
        "LEADER" -> {
            Row(modifier = Modifier.fillMaxWidth()) {
                ModifyButton(navController, meetingId, bookingViewModel, context,
                    Modifier
                        .weight(1f)
                        .fillMaxWidth())
                StartButton(navController, meetingId, bookingViewModel, context,
                    Modifier
                        .weight(1f)
                        .fillMaxWidth())
            }
        }
        "PARTICIPANT" ->
            ExitButton(navController,meetingId,bookingViewModel,context)
        "WAITING" ->
            WaitingButton()
        else -> {
            // 게스트
            JoinRequestButton(navController = navController,meetingId,bookingViewModel,context)
        }
    }
}

// 상태가 ONGOING 일 때,
@Composable
fun ongoingBottomBar(memberRole:String,meetingId:Long,bookingViewModel: BookingViewModel,context:Context ,navController:NavController){
    when (memberRole) {
        "LEADER" -> {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    AttendCheckButton(navController, meetingId, bookingViewModel, context,
                        Modifier
                            .weight(1f)
                            .fillMaxWidth())
                    PayRequestButton(navController, meetingId, bookingViewModel, context,
                        Modifier
                            .weight(1f)
                            .fillMaxWidth())
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    RestartButton(navController, meetingId, bookingViewModel, context,
                        Modifier
                            .weight(1f)
                            .fillMaxWidth())
                    EndButton(navController, meetingId, bookingViewModel, context,
                        Modifier
                            .weight(1f)
                            .fillMaxWidth())
                }
            }
        }
        "PARTICIPANT" -> {
            Row(modifier = Modifier.fillMaxWidth()) {
                AttendCheckButton(navController, meetingId, bookingViewModel, context,
                    Modifier
                        .weight(1f)
                        .fillMaxWidth())
                PayRequestButton(navController, meetingId, bookingViewModel, context,
                    Modifier
                        .weight(1f)
                        .fillMaxWidth())
            }
        }
        else -> {
            // 게스트
            AlreadyOngoingButton()
        }
    }
}
// 상태가 FINISH 일 때,
@Composable
fun finishBottomBar(memberRole:String,meetingId:Long){
    AlreadyEndButton()
}

// 모임 가입 신청 버튼
@Composable
fun JoinRequestButton(navController: NavController,meetingId:Long,bookingViewModel: BookingViewModel,context: Context){
    var refreshKey by remember { mutableStateOf(0) }
    LaunchedEffect(refreshKey) {
        bookingViewModel.getBookingDetail(meetingId)
        bookingViewModel.getParticipants(meetingId)
        bookingViewModel.getWaitingList(meetingId)

    }
    // 모임 참가신청
    Button(onClick = {
        refreshKey++
        val request = BookingJoinRequest(meetingId = meetingId)
        bookingViewModel.postBookingJoin(meetingId, request)
        val toastTop = Toast.makeText(context, "참가신청이 완료됐습니다.", Toast.LENGTH_LONG)
        toastTop.show()
        // 버튼을 누르면 BookingParticipants 탭으로 이동하도록
        navController.navigate("bookingDetail/$meetingId")
    },
        modifier = Modifier
            .fillMaxWidth(0.90f) // 화면의 95% 크기
            .padding(bottom = 5.dp)
            .height(50.dp),
        colors = ButtonDefaults . buttonColors (containerColor =
        Color(0xFF00C68E)),
        shape = RoundedCornerShape(6.dp) // 각진 모서리
    ) {
        Text("모임 참가 신청하기",
            textAlign = TextAlign.Center, // 텍스트 가운데 정렬,
            fontWeight = FontWeight.SemiBold)
    }

}
// 모임 확정 버튼
@Composable

fun StartButton(
    navController: NavController,
    meetingId: Long,
    bookingViewModel: BookingViewModel,
    context: Context,
    modifier: Modifier = Modifier
){
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Button(
            onClick = { navController.navigate(AppNavItem.BookingSetLocation.route) },
            modifier = Modifier
                .fillMaxWidth(0.95f) // 화면의 95% 크기
                .padding(bottom = 5.dp)
                .height(50.dp),
            colors = ButtonDefaults . buttonColors (containerColor =
            Color(0xFF00C68E)),
            shape = RoundedCornerShape(4.dp) // 각진 모서리
        ) {
            Text(
                text = "확정하기",
                textAlign = TextAlign.Center, // 텍스트 가운데 정렬,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
// 모임 수정 버튼
@Composable
fun ModifyButton(
    navController: NavController,
    meetingId: Long,
    bookingViewModel: BookingViewModel,
    context: Context,
    modifier: Modifier = Modifier
){
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Button(
            onClick = { navController.navigate(AppNavItem.BookingSetTitle.route) },
            modifier = Modifier
                .fillMaxWidth(0.95f) // 화면의 95% 크기
                .padding(bottom = 5.dp)
                .height(50.dp),
            colors = ButtonDefaults . buttonColors (containerColor =
            Color(0xFF00C68E)),
            shape = RoundedCornerShape(4.dp) // 각진 모서리
        ) {
            Text(
                text = "수정하기",
                textAlign = TextAlign.Center, // 텍스트 가운데 정렬,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// 모임 나가기 버튼
@Composable
fun ExitButton(
    navController: NavController,
    meetingId: Long,
    bookingViewModel: BookingViewModel,
    context: Context
){
    val postBookingExitResponse = bookingViewModel.postBookingExitResponse.observeAsState()

    LaunchedEffect(postBookingExitResponse.value) {
        postBookingExitResponse.value?.let {
            if (it.isSuccessful) {
                val toastTop = Toast.makeText(context, "모임에서 퇴장하였습니다.", Toast.LENGTH_LONG)
                toastTop.setGravity(Gravity.TOP, 0, 0)
                toastTop.show()
                navController.navigate(AppNavItem.Main.route)
            } else {
                val errorMessage = it.errorBody()?.charStream()
                try {
                    val gson = Gson()
                    val errorResponse = gson.fromJson(errorMessage, ErrorResponse::class.java)

                    val toastTop = Toast.makeText(context, errorResponse.message, Toast.LENGTH_LONG)

                    toastTop.show()
                } catch (e: JsonSyntaxException) {
                    val toastTop = Toast.makeText(context, "알 수 없는 에러", Toast.LENGTH_LONG)

                    toastTop.show()
                }
            }
        }
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = {
                bookingViewModel.postBookingExit(meetingId)
                      },
            modifier = Modifier
                .fillMaxWidth(0.95f) // 화면의 95% 크기
                .padding(bottom = 5.dp)
                .height(50.dp),
            colors = ButtonDefaults . buttonColors (containerColor =
            Color(0xFF00C68E)),
            shape = RoundedCornerShape(6.dp) // 각진 모서리
        ) {
            Text(
                text = "나가기",
                textAlign = TextAlign.Center, // 텍스트 가운데 정렬,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
// 모임 출첵 버튼
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun AttendCheckButton(
    navController : NavController,
    meetingId : Long,
    bookingViewModel : BookingViewModel,
    context : Context,
    modifier: Modifier = Modifier
){
    val patchBookingAttendResponse = bookingViewModel.patchBookingAttendResponse.observeAsState()
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    var location by remember { mutableStateOf<Location?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { loc: Location? ->
                location = loc
            }
        } else {
            Toast.makeText(context, "위치 권한이 필요합니다.", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    LaunchedEffect(patchBookingAttendResponse.value) {
        patchBookingAttendResponse.value?.let {
            if(it.isSuccessful) {
                val toastTop = Toast.makeText(context, "출석체크가 완료되었습니다.", Toast.LENGTH_LONG)
                toastTop.setGravity(Gravity.TOP, 0, 0)
                toastTop.show()
            } else {
                val errorMessage = it.errorBody()?.charStream()
                try {
                    val gson = Gson()
                    val errorResponse = gson.fromJson(errorMessage, ErrorResponse::class.java)

                    val toastTop = Toast.makeText(context, errorResponse.message, Toast.LENGTH_LONG)

                    toastTop.show()
                } catch (e: JsonSyntaxException) {
                    val toastTop = Toast.makeText(context, "알 수 없는 에러", Toast.LENGTH_LONG)

                    toastTop.show()
                }
            }
        }
    }

    // 출석체크 버튼
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Button(
            onClick = {
                location?.let { loc ->
                    val request = BookingAttendRequest(
                        meetingId = meetingId,
                        lat = loc.latitude, // 현재 위치의 위도 사용
                        lgt = loc.longitude // 현재 위치의 경도 사용
                    )
                    bookingViewModel.patchBookingAttend(request)

                } ?: run {
                    Log.d("LocationError", "Location is null")
                    // 위치 정보가 없는 경우 처리
                    Toast.makeText(context, "위치 정보를 가져올 수 없습니다.", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.95f) // 화면의 95% 크기
                .padding(bottom = 5.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor =
                Color(0xFF00C68E)
            ),
            shape = RoundedCornerShape(6.dp) // 각진 모서리
        ) {
            Text(
                "출석 체크",
                textAlign = TextAlign.Center, // 텍스트 가운데 정렬,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
// 모임 종료 버튼
@Composable
fun EndButton(
    navController : NavController,
    meetingId : Long,
    bookingViewModel : BookingViewModel,
    context : Context,
    modifier: Modifier = Modifier
){
    val patchBookingEndResponse = bookingViewModel.patchBookingEndResponse.observeAsState()

    LaunchedEffect(patchBookingEndResponse.value) {
        patchBookingEndResponse.value?.let {
            if (it.isSuccessful) {
                val toastTop = Toast.makeText(context, "모임이 종료되었습니다.", Toast.LENGTH_LONG)
                toastTop.show()
                navController.navigate(AppNavItem.Main.route)
            } else {
                val errorMessage = it.errorBody()?.charStream()
                try {
                    // Gson을 사용하여 파싱
                    val gson = Gson()
                    val errorResponse = gson.fromJson(errorMessage, ErrorResponse::class.java)

                    val toastTop = Toast.makeText(context, "${errorResponse.message}", Toast.LENGTH_LONG)

                    toastTop.show()
                } catch (e: JsonSyntaxException) {
                    val toastTop = Toast.makeText(context, "알 수 없는 에러", Toast.LENGTH_LONG)

                    toastTop.show()
                }
            }
        }
    }

    // 모임종료 버튼
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Button(
            onClick = {
                bookingViewModel.patchBookingEnd(meetingId)
            },
            modifier = Modifier
                .fillMaxWidth(0.95f) // 화면의 95% 크기
                .padding(bottom = 5.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor =
                Color(0xFF00C68E)
            ),
            shape = RoundedCornerShape(6.dp) // 각진 모서리
        ) {
            Text(
                "모임 종료",
                textAlign = TextAlign.Center, // 텍스트 가운데 정렬,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
// 모임 다시 시작(한 번 더 ) 버튼
@Composable
fun RestartButton(
    navController : NavController,
    meetingId : Long,
    bookingViewModel : BookingViewModel,
    context : Context,
    modifier: Modifier = Modifier
) {
    val patchBookingRestartResponse = bookingViewModel.patchBookingRestartResponse.observeAsState()

    LaunchedEffect(patchBookingRestartResponse.value) {
        patchBookingRestartResponse.value?.let {
            if (it.isSuccessful) {
                val toastTop = Toast.makeText(context, "한 번 더하기.", Toast.LENGTH_LONG)
                toastTop.setGravity(Gravity.TOP, 0, 0)
                toastTop.show()
                navController.navigate("")
            } else {
                val errorMessage = it.errorBody()?.charStream()
                try {
                    // Gson을 사용하여 파싱
                    val gson = Gson()
                    val errorResponse = gson.fromJson(errorMessage, ErrorResponse::class.java)

                    val toastTop = Toast.makeText(context, "${errorResponse.message}", Toast.LENGTH_LONG)

                    toastTop.show()
                } catch (e: JsonSyntaxException) {
                    // 에러 처리
                    Log.e("테스트중", "Gson 파싱 에러: ", e)
                    val toastTop = Toast.makeText(context, "알 수 없는 에러", Toast.LENGTH_LONG)

                    toastTop.show()
                }
            }
        }
    }
    // 한번 더 하기 버튼
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Button(
            onClick = {
                bookingViewModel.patchBookingRestart(meetingId)
            },
            modifier = Modifier
                .fillMaxWidth(0.95f) // 화면의 95% 크기
                .padding(bottom = 5.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor =
                Color(0xFF00C68E)
            ),
            shape = RoundedCornerShape(6.dp) // 각진 모서리
        ) {
            Text(
                "한번 더 하기",
                textAlign = TextAlign.Center, // 텍스트 가운데 정렬,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// 참가비 결제 버튼
@Composable
fun PayRequestButton(
    navController : NavController,
    meetingId : Long,
    bookingViewModel : BookingViewModel,
    context : Context,
    modifier: Modifier = Modifier
){
    val patchPaymentResponse by bookingViewModel.patchPaymentResponse.observeAsState()
    val getBookingDetailResponse by bookingViewModel.getBookingDetailResponse.observeAsState()
    
    LaunchedEffect(patchPaymentResponse) {
        patchPaymentResponse?.let {
            if (it.isSuccessful) {
                val toastTop = Toast.makeText(context, "참가비 결제가 완료되었습니다.", Toast.LENGTH_LONG)
                toastTop.setGravity(Gravity.TOP, 0, 0)
                toastTop.show()
            } else {
                val errorMessage = it.errorBody()?.charStream()
                try {
                    // Gson을 사용하여 파싱
                    val gson = Gson()
                    val errorResponse = gson.fromJson(errorMessage, ErrorResponse::class.java)

                    val toastTop = Toast.makeText(context, "${errorResponse.message}", Toast.LENGTH_LONG)

                    toastTop.show()

                    if (errorResponse.message == "포인트가 부족합니다.") {
                        getBookingDetailResponse?.let {
                            it.body()?.let {
                                val fee = it.meetingInfoList[0].fee
                                navController.navigate("pay/ready/$fee")
                            }
                        }
                    }
                } catch (e: JsonSyntaxException) {
                    // 에러 처리
                    Log.e("테스트중", "Gson 파싱 에러: ", e)
                    val toastTop = Toast.makeText(context, "알 수 없는 에러", Toast.LENGTH_LONG)

                    toastTop.show()
                }
            }
        }
    }
    
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
    Button(
        onClick = {
            bookingViewModel.patchPayment(meetingId)
        },
        modifier = Modifier
            .fillMaxWidth(0.95f) // 화면의 95% 크기
            .padding(bottom = 5.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor =
            Color(0xFF00C68E)
        ),
        shape = RoundedCornerShape(6.dp) // 각진 모서리
    ) {
        Text(
            "참가비 결제",
            textAlign = TextAlign.Center, // 텍스트 가운데 정렬,
            fontWeight = FontWeight.SemiBold
        )
    }
}
}

// 참가신청 승인 대기 버튼 ( 기다리는 중..)
@Composable
fun WaitingButton() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
          Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth(0.95f) // 화면의 95% 크기
                    .padding(bottom = 5.dp)
                    .height(50.dp),
                    colors = ButtonDefaults . buttonColors (containerColor =
                Color(0xff2f4858)),
            shape = RoundedCornerShape(6.dp) // 각진 모서리
        ) {
            Text(
                text = "가입 승인을 기다리고 있습니다.",
                textAlign = TextAlign.Center, // 텍스트 가운데 정렬,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
// 이미 참가한 사람에게 보여지는 버튼
@Composable
fun AlreadyJoinButton() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth(0.95f) // 화면의 95% 크기
                .padding(bottom = 5.dp)
                .height(50.dp),
            colors = ButtonDefaults . buttonColors (containerColor =
            Color(0xff2f4858)),
            shape = RoundedCornerShape(6.dp) // 각진 모서리
        ) {
            Text(
                text = "이미 참가중인 모임입니다.",
                textAlign = TextAlign.Center, // 텍스트 가운데 정렬,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

}

// 이미 진행중인 모임에 대해 보여지는 버튼
@Composable
fun AlreadyOngoingButton() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth(0.95f) // 화면의 95% 크기
                .padding(bottom = 5.dp)
                .height(50.dp),
            colors = ButtonDefaults . buttonColors (containerColor =
            Color(0xff2f4858)),
            shape = RoundedCornerShape(6.dp) // 각진 모서리
        ) {
            Text(
                text = "이미 진행중인 모임입니다.",
                textAlign = TextAlign.Center, // 텍스트 가운데 정렬,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
// 이미 종료된 모임에 대해 보여지는 버튼
@Composable
fun AlreadyEndButton(){
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth(0.95f) // 화면의 95% 크기
                .padding(bottom = 5.dp)
                .height(50.dp),
            colors = ButtonDefaults . buttonColors (containerColor =
            Color(0xff2f4858)),
            shape = RoundedCornerShape(6.dp) // 각진 모서리
        ) {
            Text(
                text = "종료된 모임입니다.",
                textAlign = TextAlign.Center, // 텍스트 가운데 정렬,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

}
