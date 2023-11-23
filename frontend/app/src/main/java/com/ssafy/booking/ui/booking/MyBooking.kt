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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ssafy.booking.R
import com.ssafy.booking.di.App
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.ui.common.TabBar
import com.ssafy.booking.ui.common.TopBar
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.domain.model.booking.BookingAll
import com.ssafy.domain.model.booking.BookingListByMemberPk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBooking(
    navController: NavController,
    appViewModel: AppViewModel
) {
    // 뷰모델 연결
    val bookingViewModel: BookingViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        val memberPk = App.prefs.getMemberPk()
        bookingViewModel.getBookingByMemberPk(memberPk)
    }

    ///////
    Scaffold(
        topBar = {
            TopBar("나의 북킹")
        },
        bottomBar = {
            BottomNav(navController, appViewModel)
        },
        floatingActionButton = {
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .fillMaxHeight()
        ) {
            BookingListByMemberPk(navController, appViewModel, bookingViewModel)
        }
    }
}

@Composable
fun BookingListByMemberPk(
    navController: NavController,
    appViewModel: AppViewModel,
    bookingViewModel: BookingViewModel
) {
    val getBookingByMemberPkResponse by bookingViewModel.getBookingByMemberPkResponse.observeAsState()
    val bookingList = getBookingByMemberPkResponse?.body()

    // meetingState에 따라 그룹화
    val groupedBookings = bookingList?.groupBy { it.meetingState }
    val onGoingBookings = groupedBookings?.get("ONGOING")
    val finishBookings = groupedBookings?.get("FINISH")
    val preparingBookings = groupedBookings?.get("PREPARING")

    if (groupedBookings == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_main),
                contentDescription = "배경 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Text("현재 참여중인 북킹이 없습니다.",fontWeight = FontWeight.Medium, modifier = Modifier.offset(y=(-70).dp))
            Button(
                onClick = { navController.navigate("create/booking/isbn") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C68E)),
                shape = RoundedCornerShape(3.dp),
                modifier = Modifier
                    .padding(10.dp)
                    .offset(y = (-120).dp)) {
                Text("북킹 시작하기", color= Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 26.sp)
            }
        }
    } else {
        TabBar(tabTitles = listOf("진행중 모임", "완료된 모임", "준비중 모임"), contentForTab={ idx ->
            when (idx) {
                0 -> newBookingItemByMemberPk(onGoingBookings, navController)
                1 -> newBookingItemByMemberPk(finishBookings, navController)
                2 -> newBookingItemByMemberPk(preparingBookings, navController)
            }
        })
    }
}

@Composable
fun newBookingItemByMemberPk(
    myBookingList : List<BookingListByMemberPk>?,
    navController : NavController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .padding(horizontal = 8.dp, vertical = 15.dp)
    ) {
        myBookingList?.let {
            items(it.size) {idx ->
                BookingItemByMemberPk(it[idx], navController)
            }
        } ?: run {
            item() {
                Text(text = "모임이 없습니다.")
            }
        }


//        if (groupedBookings == null) {
//            item {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.bg_main),
//                        contentDescription = "배경 이미지",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier.fillMaxSize()
//                    )
//                    Button(onClick = {
//                        navController.navigate(AppNavItem.CreateBooking.route)
//                    }) {
//                        Text("북킹 생성하기")
//                    }
//                }
//            }
//        } else {


//                // 상태별로 섹션 렌더링
//                groupedBookings.let { groups ->
//                    groups.forEach { (state, bookings) ->
//                        item {
//                            Text(
//                                text = when (state) {
//                                    "ONGOING" -> "진행 중인 모임"
//                                    "FINISH" -> "완료된 모임"
//                                    //                            "PREPARING" -> "준비 중인 모임"
//                                    else -> "준비 중인 모임"
//                                },
//                                fontWeight = FontWeight.ExtraBold,
//                                fontSize = 24.sp,
//                                modifier = Modifier.padding(vertical = 4.dp)
//                            )
//                        }
//                        items(bookings) { booking ->
//                            BookingItemByMemberPk(booking, navController)
//                        }
//                    }
//                }
//        }
    }
}


@Composable
fun BookingItemByMemberPk(bookingItem: BookingListByMemberPk, navController: NavController) {
    val meetingId = bookingItem.meetingId
    Log.d("TEST", "asDas${bookingItem.meetingInfoList}")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { // clickable 모디파이어 추가
                navController.navigate("bookingDetail/$meetingId") // 클릭 시 상세 화면으로 이동
                App.prefs.putMeetingId(meetingId) // 미팅 아이디 SharedPreference에 저장하기
            }
    ) {
        Image(
            painter = // 이미지가 로딩될 때 페이드인 효과 적용
            rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = bookingItem.coverImage)
                    .apply(block = fun ImageRequest.Builder.() {
                        crossfade(true) // 이미지가 로딩될 때 페이드인 효과 적용
                    }).build()
            ),
            contentDescription = "Book Image",
            modifier = Modifier
                .size(80.dp, 100.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop // 이미지의 비율 유지하면서 영역 채우기
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = bookingItem.meetingTitle,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = bookingItem.bookTitle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    bookingItem.address.let{
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = "locate",
                        modifier = Modifier.size(12.dp),
                        tint = Color.Gray
                    )
                    Text(
        //                    text = booking.lat.toString(),
                        text = bookingItem.address ?: "",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (hashtag in bookingItem.hashtagList) {
                    HashtagChip(tag = hashtag.content, id = hashtag.hashtagId) // 각 해시태그에 대한 칩 생성
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Participants Icon",
                    modifier = Modifier.size(12.dp),
                    tint = Color.Gray
                )
                Text(
                    text = "${bookingItem.curParticipants}명/${bookingItem.maxParticipants}명",
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 2.dp),
                    color = Color.Gray
                )
            }
        }
    }
    Divider(
        color = Color.LightGray,
        thickness = 0.4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    )
}
// 해시태그 칩
