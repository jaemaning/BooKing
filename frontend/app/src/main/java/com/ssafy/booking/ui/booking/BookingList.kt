package com.ssafy.booking.ui.booking

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.runtime.remember
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
import coil.compose.rememberImagePainter
import com.ssafy.booking.R
import com.ssafy.booking.di.App
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.utils.MyFirebaseMessagingService
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.booking.viewmodel.LocationViewModel
import com.ssafy.data.repository.token.TokenDataSource
import com.ssafy.domain.model.DeviceToken
import com.ssafy.domain.model.booking.BookingAll
import com.ssafy.domain.model.booking.BookingListByTitle
import hilt_aggregated_deps._com_ssafy_booking_di_App_GeneratedInjector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(
    navController: NavController,
    appViewModel: AppViewModel
) {
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val locationViewModel : LocationViewModel = hiltViewModel()
    val userInfoState by bookingViewModel.getUserInfoResponse.observeAsState()
    val context = LocalContext.current
    val tokenDataSource = TokenDataSource(context)
    val loginId = tokenDataSource.getLoginId()
    val deviceToken: String? = tokenDataSource.getDeviceToken()
    var myLocation by remember { mutableStateOf("대한민국") }
    MyFirebaseMessagingService.getFirebaseToken { token ->
        val tokenDataSource = TokenDataSource(context)
        tokenDataSource.putDeviceToken(token)
    }

    // 검색어 상태를 저장하는 변수
    var searchQuery by remember { mutableStateOf("") }

    // 검색 결과 상태를 저장하는 변수
    val searchResultState by bookingViewModel.getBookingByTitleResponse.observeAsState()

    // 검색어가 변경될 때마다 검색 함수를 호출
    LaunchedEffect(searchQuery) {
        bookingViewModel.getBookingByTitle(searchQuery)
    }

    // LaunchedEffect를 사용하여 한 번만 API 호출
    LaunchedEffect(Unit) {
        bookingViewModel.postDeivceToken(DeviceToken(deviceToken))
        bookingViewModel.getUserInfo(loginId!!)
        bookingViewModel.getBookingAllList()
        // 위치 세팅
        val lat = App.prefs.getLat().toString()
        val lgt = App.prefs.getLgt().toString()
        locationViewModel.getAddress(lat, lgt)

    }
    val addressResponse by locationViewModel.getAddressResponse.observeAsState()
    LaunchedEffect(addressResponse) {
        val address = addressResponse?.body()?.documents?.firstOrNull()?.address?.addressName ?: "위치"
        val address1 = addressResponse?.body()?.documents?.firstOrNull()?.address?.region2DepthName ?: "위치"
        val address2 = addressResponse?.body()?.documents?.firstOrNull()?.address?.region3DepthName ?: "불러오는 중..."
        myLocation = "$address1 $address2"
        App.prefs.putUserAddress(address)
        App.prefs.putShortUserAddress(myLocation?:"")
    }
    LaunchedEffect(userInfoState) {
        userInfoState?.body()?.let {
            tokenDataSource.putNickName(it.nickname)
            tokenDataSource.putProfileImage(it.profileImage)
            tokenDataSource.putMemberPk(it.memberPk)
            tokenDataSource.putLat(it.lat.toFloat())
            tokenDataSource.putLgt(it.lgt.toFloat())

        }
    }
    Scaffold(
        topBar = {
            HomeTopBar(navController, appViewModel, myLocation, bookingViewModel, searchQuery, onSearchQueryChanged = { query ->
                searchQuery = query
            })
        },
        bottomBar = {
            BottomNav(navController, appViewModel)
        },
        floatingActionButton = {
            MyFloatingActionButton(navController, appViewModel)
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .fillMaxHeight()
        ) {
            if (searchQuery.isEmpty()) {
                // 검색어가 비어있으면 기존 모임 목록을 표시
                BookList(navController, appViewModel, bookingViewModel)
            } else {
                // 검색어가 있으면 검색 결과를 보여주는 Composable을 표시
                SearchResultsList(searchResultState?.body(),navController)
            }
            }
        }
    }
@Composable
fun BookList(navController: NavController, appViewModel: AppViewModel,bookingViewModel: BookingViewModel) {
    val bookingAllListState by bookingViewModel.getBookingAllList.observeAsState()
    // response가 not null 이면 바디 추출
    val bookingAllList = bookingAllListState?.body()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .padding(horizontal = 8.dp, vertical = 15.dp)
    ) {
        bookingAllList?.let { bookings ->
            items(bookings) { booking ->
                BookItem(booking = booking,navController)
            }
        }
        }
    }
@Composable
fun BookItem(booking: BookingAll,navController: NavController) {
    val meetingId = booking.meetingId
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
            painter = rememberImagePainter(
                data = booking.coverImage,
                builder = {
                    crossfade(true) // 이미지가 로딩될 때 페이드인 효과 적용
                }
            ),
            contentDescription = "Book Image",
            modifier = Modifier
                .size(80.dp, 100.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop // 이미지의 비율 유지하면서 영역 채우기
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = booking.meetingTitle, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(
//                    painter = painterResource(id = R.drawable.outline_auto_stories_24),
//                    contentDescription = "녹음 중지",
//                    modifier = Modifier
//                        .size(14.dp),
//                    tint = Color(0xFF000000)
//                )
                Text(
                    text = booking.bookTitle,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                booking?.let {
                    booking.address?.let {
                        Icon(Icons.Outlined.LocationOn, contentDescription = "locate", modifier = Modifier.size(12.dp), tint = Color.Gray)
                        Text(
        //                    text = booking.lat.toString(),
                            text = booking.address?:"",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (hashtag in booking.hashtagList) {
                    HashtagChip(tag = hashtag.content,id = hashtag.hashtagId) // 각 해시태그에 대한 칩 생성
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
                    text = "${booking.curParticipants}명/${booking.maxParticipants}명",
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
@Composable
fun HashtagChip(tag: String, id: Long) {
    val navController = LocalNavigation.current
    var tagColor by remember { mutableStateOf(0xFF000000) }
    when ((id.toInt())%4) {
        0 -> tagColor = 0xFF12BD7E
        1 ->tagColor = 0xFF005723
        2 -> tagColor = 0xFF0072C3
        3 -> tagColor = 0xFF00C1FF
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(end = 5.dp) // 오른쪽 마진
            .border(0.8.dp, Color(tagColor?:0xFF12BD7E), RoundedCornerShape(3.dp)) // #12BD7E 색상의 테두리 추가
            .background(Color.White, RoundedCornerShape(3.dp)) // 흰색 배경
            .padding(horizontal = 4.dp, vertical = 4.dp) // 내부 패딩
//            .padding(end = 4.dp) // 오른쪽 마진
//            .background(Color(0xFF00C68E), RoundedCornerShape(10.dp)) // 둥근 사각형의 배경
//            .padding(horizontal = 8.dp, vertical = 4.dp) // 내부 패딩
            .clickable {
                navController.navigate("booking/search/hashtag/$id/$tag")
            }
    ) {
        Text(
            text = "#${tag}",
            color = Color(tagColor?:0xFF12BD7E), // 텍스트 색상을 #12BD7E로 변경
//            color = Color.White,
            fontSize = 10.sp, // 작은 글씨 크기
            maxLines = 1,
            overflow = TextOverflow.Ellipsis // 글이 넘치면 말줄임표로 처리
        )
    }
}

@Composable
fun MyFloatingActionButton(navController: NavController, appViewModel: AppViewModel) {
    FloatingActionButton(
        onClick = { navController.navigate("create/booking/isbn") },
        modifier = Modifier
            .padding(end = 16.dp, bottom = 10.dp)
            .size(65.dp),
        containerColor = Color(0xFF12BD7E),
        shape = CircleShape
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = "Localized description",
            modifier = Modifier.size(40.dp),
            tint = Color.White

        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(navController: NavController, appViewModel: AppViewModel,myLocation:String,bookingViewModel: BookingViewModel,searchQuery: String,
               onSearchQueryChanged: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF12BD7E),
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            ) // 배경색과 모서리를 둥글게 설정
            .height(128.dp)
            .padding(top=5  .dp),
    ) {
        // 상단의 하남동과 설정 아이콘
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.CenterStart)
//                    .padding(top = 10.dp)
                    .clickable {
                        navController.navigate("setting/address")
                    }
            ) {
                Text(text = myLocation, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFffffff))
                Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = Color(0xFFffffff))
            }
            Icon(

                Icons.Rounded.Settings,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { // 클릭 가능하도록 설정
                        navController.navigate(AppNavItem.Setting.route) {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                tint = Color(0xFFffffff)
            )
        }
        }
        OutlinedTextField(
            value = searchQuery, // 이 부분을 뷰모델의 상태로 연결하거나 필요에 따라 변경
            onValueChange = onSearchQueryChanged,
            placeholder = { Text("모임의 제목을 입력해주세요.", fontSize = 11.sp, color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 55.dp)
                .padding(bottom = 16.dp)
//                .height(50.dp)
                .background(Color.White, shape = RoundedCornerShape(3.dp)),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF12BD7E),
                unfocusedBorderColor = Color.White
            ),
            textStyle = TextStyle(color = Color.Gray, fontSize = 11.sp, baselineShift = BaselineShift.None),
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = Color(0xFF12BD7E)) }
        )
    }

@Composable
fun SearchResultsList(
    searchResults: List<BookingAll>?,
    navController: NavController
) {
    LazyColumn {
        searchResults?.let { results ->
            items(results) { booking ->
                BookItem(booking = booking, navController)
            }
        } ?: item {
//            Text("검색 결과가 없습니다.")
        }
    }
}