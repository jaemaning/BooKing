package com.ssafy.booking.ui.location

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
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
import com.ssafy.booking.ui.common.BackTopBar
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.LocationViewModel
import com.ssafy.booking.viewmodel.MyPageViewModel
import com.ssafy.domain.model.mypage.AddressnModifyRequest

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun SettingAddress(
    navController: NavController,
    appViewModel: AppViewModel
) {
    val myPageViewModel: MyPageViewModel = hiltViewModel()
    val locationViewModel: LocationViewModel = hiltViewModel()
    val getAddressResponse by locationViewModel.getAddressResponse.observeAsState()
    val (myLocation, setMyLocation) = remember { mutableStateOf("0") }
    val (addressName, setAddressName) = remember { mutableStateOf("") }
    val context = LocalContext.current
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    var location by remember { mutableStateOf<Location?>(null) }
    val launcher = rememberLauncherForActivityResult(RequestPermission()) { isGranted ->
        if (isGranted) {
            val locationResult: Task<Location> = fusedLocationProviderClient.lastLocation
            locationResult.addOnSuccessListener { loc: Location? ->
                location = loc
            }
        }
    }
    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
    LaunchedEffect(location) {
        setAddressName(
            getAddressResponse?.body()?.documents?.firstOrNull()?.address?.addressName ?: ""
        )
    }

    Scaffold(
        topBar = {
            BackTopBar(title = "내 동네 설정")
        }, bottomBar = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF00C68E))
                    .clickable {
                        // 위치 값을 입력받았을 때만 수정하기.
                        if (location != null) {
                            Log.d("위치수정", location.toString())
                            myPageViewModel.patchUserAddress(
                                AddressnModifyRequest(
                                    address = location.toString()
                                )
                            )
                            navController.navigate(AppNavItem.Main.route)
                        } else {
                            Log.d("위치수정", "실패")
                        }
                    }
            ) {
                Text(
                    text = "내 동네 설정하기",
                    modifier = Modifier.padding(16.dp),
                    Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)

        ) {
            LaunchedEffect(Unit) {
                launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            Column(
                modifier = Modifier
                    .fillMaxSize(), // 최대 크기를 채움
//                verticalArrangement = Arrangement.SpaceAround,
            ) {
                SettingLocationMap()
                OriginalMyLocation()
                addressName.let {
                    Column(
                        modifier = Modifier.padding(8.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "새 위치 불러오기", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Row {
                            IconButton(
                                onClick = {
                                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                    Log.d("위치", location.toString())
                                    Log.d("위도", location?.latitude.toString())
                                    Log.d("경도", location?.longitude.toString())
                                    setMyLocation(location.toString())
                                    val (lat, lgt) = extractLatitudeLongitude(myLocation)
                                        ?: return@IconButton

                                    Log.d("위치", myLocation)
                                    Log.d("위치위도", lat.toString())
                                    Log.d("위치경도", lgt.toString())
                                    // 경도,위도 쏴서 주소 받아오기.
                                    locationViewModel.getAddress(lat.toString(), lgt.toString())

                                    // 이상한 곳에 위경도를 쏘면 주소가 안 뜨는데, 그럴 때는 주소를 불러오지 않음.
                                    if (getAddressResponse?.body()?.documents?.isNotEmpty() == true) {
                                        setAddressName(
                                            getAddressResponse?.body()?.documents?.firstOrNull()?.address?.addressName
                                                ?: return@IconButton
                                        )
                                    }
                                },
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_my_location_24),
                                    contentDescription = null,
                                    tint = Color(0xFF007B58),
                                    modifier = Modifier.size(56.dp)
                                )
                            }
                            Text(text = addressName)
                        }
                    }
                }
            }
        }
    }
}

// MyLocaton에서 위도,경도 추출하는 함수.

@Composable
fun OriginalMyLocation() {
    val context = LocalContext.current
    val address = App.prefs.getUserAddress()
    Column(
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "기존 위치", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = address ?: "유저 주소가 없습니다.")
    }

}

fun extractLatitudeLongitude(location: String): Pair<Double, Double>? {
    val regex = """Location\[fused (\d+\.\d+),(-?\d+\.\d+)""".toRegex()
    val matchResult = regex.find(location)

    return matchResult?.let {
        val (latitude, longitude) = it.destructured
        Pair(latitude.toDouble(), longitude.toDouble())
    }
}

@SuppressLint("MissingPermission")
@Composable
fun currentMyLocation() {

}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun SettingLocationMap() {
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(maxZoom = 20.0, minZoom = 5.0)
        )
    }
    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(isLocationButtonEnabled = false)
        )
    }

    // meetingInfo의 위도, 경도를 사용하여 약속장소
    val initLat = App.prefs.getLat().toDouble()
    val initLgt = App.prefs.getLgt().toDouble()
    val currentLocation = LatLng(initLat, initLgt)
    Log.d("위치1", initLat.toString())
    Log.d("위치2", initLgt.toString())
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 meetingInfo의 위치로 설정합니다.
        position = CameraPosition(currentLocation, 15.0)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth() // 전체 너비를 채움
            .padding(horizontal = 30.dp) // 좌우 여백 추가
            .fillMaxHeight(0.8f) // 높이를 화면의 50%로 설정
    ) {
        NaverMap(
            properties = mapProperties,
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState
        ) {
            val context = LocalContext.current
            Marker(
                state = MarkerState(position = currentLocation),
                captionText = "기존 내 동네"
            )
            CircleOverlay(
                center = currentLocation, radius = 10000.0, // 10km
                color = Color(0, 0, 255, 0) // 반투명한 파란색, ]
            )
        }
    }
}