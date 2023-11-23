package com.ssafy.booking.ui.booking.bookingSetting

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.CircleOverlay
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.ssafy.booking.R
import com.ssafy.booking.di.App
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.BackTopBar
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.booking.viewmodel.LocationViewModel
import com.ssafy.domain.model.loacation.KakaoSearchResponse

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SetLocation() {
    val locationViewModel: LocationViewModel = hiltViewModel()
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val getKakaoSearchResponse by locationViewModel.getKakaoSearchResponse.observeAsState()
    val locationState by bookingViewModel.location.observeAsState()
    val latState by bookingViewModel.lat.observeAsState()
    val lgtState by bookingViewModel.lgt.observeAsState()
    val placeNameState by bookingViewModel.placeName.observeAsState()
    val showSearchResults = remember { mutableStateOf(true) }
    Scaffold(
        topBar = {
            BackTopBar(title = "모임 장소 선택")
        },
        bottomBar = {
            // 바텀 버튼을 Scaffold의 bottomBar로 설정합니다.
            SetLocationBottomButton(locationState, placeNameState)
        }
    )

    { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SetLocationSearch(locationViewModel, showSearchResults)
            SearchResult(bookingViewModel, locationViewModel, showSearchResults)
            SelectedLocation(bookingViewModel, locationState, placeNameState)
            latState?.let { setLocationMap(bookingViewModel,placeName = placeNameState,showSearchResults) }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetLocationSearch(viewModel: LocationViewModel, showSearchReults: MutableState<Boolean>) {
    // 검색 창
    var location by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val lat = App.prefs.getLat().toString()
    val lgt = App.prefs.getLgt().toString()
    OutlinedTextField(
        value = location, // 이 부분을 뷰모델의 상태로 연결하거나 필요에 따라 변경
        onValueChange = { location = it },
        placeholder = { Text("모임 위치를 검색해주세요.", fontSize = 16.sp, color = Color.Gray) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .padding(bottom = 16.dp)
            .height(60.dp)
            .background(Color.White, shape = RoundedCornerShape(3.dp)),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF12BD7E),
            unfocusedBorderColor = Color.White
        ),
        textStyle = TextStyle(
            color = Color.Gray,
            fontSize = 16.sp,
            baselineShift = BaselineShift.None
        ),
        leadingIcon = {
            Icon(
                Icons.Outlined.Search,
                contentDescription = null,
                tint = Color(0xFF12BD7E),
                modifier = Modifier.size(24.dp)
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                // 사용자 설정 위치에서 반경 20km 안에서만 검색
                viewModel.getSearchList(location.text, 10, 15, lat, lgt, 20000)
                showSearchReults.value = true
            }
        )
    )
}

@Composable
fun SelectedLocation(
    bookingViewModel: BookingViewModel,
    locationState: String?,
    placeNameState: String?,
) {
    if (locationState != null || placeNameState != null) {
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = "${locationState ?: "정보 없음"}", fontSize = 20.sp)
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = "${placeNameState ?: " "}", fontSize = 20.sp)
    }
}

@Composable
fun SearchResult(
    bookingViewModel: BookingViewModel,
    locationViewModel: LocationViewModel,
    showSearchResults: MutableState<Boolean>
) {
    val getKakaoSearchResponse by locationViewModel.getKakaoSearchResponse.observeAsState()
    // 클릭시 검색결과 사라지게


    val response = getKakaoSearchResponse?.body()?.documents
    if (showSearchResults.value && response != null && response.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.padding(10.dp)
        ) {
            items(response) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .shadow(4.dp, RoundedCornerShape(2.dp))
                        .clickable {
                            // Card 클릭 시 ViewModel의 상태 업데이트
                            bookingViewModel.location.value = item.addressName
                            bookingViewModel.lat.value = item.y.toDouble()
                            bookingViewModel.lgt.value = item.x.toDouble()
                            bookingViewModel.placeName.value = item.placeName
                            showSearchResults.value = false
                            App.prefs.putMeetingLat(item.y.toFloat())
                            App.prefs.putMeetingLgt(item.x.toFloat())
                            App.prefs.putMeetingAddress(item.addressName)
                            App.prefs.putMeetingLocation(item.placeName)
                        },
                    colors = CardDefaults.cardColors(colorResource(id = R.color.booking_1)),
                    shape = RoundedCornerShape(2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "${item.placeName}",
                                color = Color.White,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.size(15.dp))
                            Text(
                                text = "${item.categoryGroupName}",
                                color = Color.LightGray
                            )
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "${item.addressName}",
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.size(15.dp))
                            Text(
                                text = "${item.distance.toInt()/1000}.${item.distance.toInt()%1000} Km",
                                color = Color.LightGray
                            )
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "${item.roadAddressName}",
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Outlined.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.size(5.dp))
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Outlined.Phone,
                                    contentDescription = null,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.size(5.dp))
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Outlined.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    } else if (showSearchResults.value && (response == null || response.isEmpty())){
        Text(text = "검색 결과가 없습니다.")
    }
}

@Composable
fun SetLocationBottomButton(
    locationState: String?,
    placeNameState: String?
) {
    // 현재 Composable 함수와 연관된 Context 가져오기
    val context = LocalContext.current
    val navController = LocalNavigation.current

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                if (locationState.isNullOrEmpty() || placeNameState.isNullOrEmpty()) {
                    // 제목 또는 내용이 비어있을 경우 Toast 메시지 표시
                    Toast.makeText(context, "독서모임을 진행할 장소를 선택해주세요.", Toast.LENGTH_LONG).show()
                } else {
                    // 모두 입력된 경우 네비게이션
                    navController.navigate(AppNavItem.BookingSetDateAndFee.route)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(3.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF00C68E))
        ) {
            androidx.wear.compose.material.Text("다음", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun setLocationMap(bookingViewModel: BookingViewModel,placeName: String?,showSearchResults: MutableState<Boolean>) {
    // ViewModel에서 위도와 경도 상태를 관찰
    val markerLat by bookingViewModel.lat.observeAsState()
    val markerLng by bookingViewModel.lgt.observeAsState()

    // 마커와 카메라 위치 상태를 기억
    val markerPositionState = remember(markerLat, markerLng) {
        MarkerState(LatLng(markerLat ?: 37.5, markerLng ?: 126.5))
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(markerLat ?: 37.5, markerLng ?: 126.5), 16.0)
    }
// markerLat와 markerLng가 변경될 때마다 카메라 위치 업데이트
    LaunchedEffect(markerLat, markerLng) {
        cameraPositionState.position = CameraPosition(LatLng(markerLat ?: 37.5, markerLng ?: 126.5), 15.0)
    }

    if ( markerLat != 0.0 && markerLng != 0.0 ) {
            Box(Modifier.height(300.dp)) {
                NaverMap(cameraPositionState = cameraPositionState) {
                    // 마커 위치 업데이트
                    Marker(
                        state = markerPositionState,
                        captionText = placeName
                    )
                }
            }
    }
//    Box(Modifier.fillMaxSize()) {
//    Box(Modifier.height(300.dp)) {
//        NaverMap(properties = mapProperties, uiSettings = mapUiSettings) {
//            // 마커 찍는 법.
//            Marker(
//                state = MarkerState(position = LatLng(CurLat,CurLng)),
//                captionText = placeName
//            )
//            Marker(
//                state = MarkerState(position = LatLng(37.390791, 127.096306)),
//                captionText = "Marker in Pangyo"
//            )
//            Marker(
//                state = MarkerState(position = markerPosition),
//                icon = MarkerIcons.BLACK, // 기본 마커 아이콘 변경
//                iconTintColor = Color.Red // 아이콘 색상 변경 (옵션)
//            )


//        }
//    }
}

