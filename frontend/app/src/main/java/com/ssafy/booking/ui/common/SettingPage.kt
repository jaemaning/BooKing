package com.ssafy.booking.ui.common

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.booking.R
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.MainActivity
import com.ssafy.booking.viewmodel.MainViewModel
import com.ssafy.booking.viewmodel.SettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingPage() {
    val navController = LocalNavigation.current
    val settingViewModel: SettingViewModel = hiltViewModel()
    val context = LocalContext.current
    val mainViewModel : MainViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "설정") }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, contentDescription = "뒤로가기"
                    )
                }
            }
//                actions = {
//                    IconButton(onClick = {}) {
//                        Icon(
//                            imageVector = Icons.Filled.Settings,
//                            contentDescription = "메뉴"
//                        )
//                    }
//                }
            )
        }, modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Text("알림")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clickable {
                                Toast.makeText(context, "준비중입니다.", Toast.LENGTH_SHORT).show()
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "알림 내역 관리"
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = "알림 내역 관리")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clickable {
                                Toast.makeText(context, "준비중입니다.", Toast.LENGTH_SHORT).show()
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info, contentDescription = "공지사항"
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text("공지사항")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clickable {
                                Toast.makeText(context, "준비중입니다.", Toast.LENGTH_SHORT).show()
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Warning, contentDescription = "신고내역"
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = "신고내역")
                    }
                }
                Text(text = "기타")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clickable {
                                Toast.makeText(context, "준비중입니다.", Toast.LENGTH_SHORT).show()
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.qr_code_fill0_wght400_grad0_opsz24),
                            contentDescription = "qr코드"
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = "앱 추천 QR 코드")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clickable {
                                Toast.makeText(context, "준비중입니다.", Toast.LENGTH_SHORT).show()
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.timeline_fill0_wght400_grad0_opsz24),
                            contentDescription = "현재 버전"
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = "현재 버전")
                        Spacer(modifier = Modifier.padding(horizontal = 50.dp))
                        Text(text = "v3.0.0", color = Color.Gray)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clickable {
                                Toast.makeText(context, "준비중입니다.", Toast.LENGTH_SHORT).show()
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info, contentDescription = "서비스 이용 약관"
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = "서비스 이용 약관")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clickable {
                                Toast.makeText(context, "준비중입니다.", Toast.LENGTH_SHORT).show()
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info, contentDescription = "개인정보 처리방침"
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = "개인정보 처리방침")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "기타 문의사항은 lovesay00@gmail.com 으로 보내주세요.",
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            settingViewModel.logout()
                            mainViewModel.signOutGoogle()
                            navController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                            }
                        }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray, // 버튼 배경 색상
                            contentColor = Color.Black // 버튼 텍스트 및 아이콘 색상
                        )
                    ) {
                        Text(text = "로그아웃")
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            val loginId = settingViewModel.getLoginId()
                            mainViewModel.signOutGoogle()
                            Log.d("jmkim", "$loginId")
                            loginId?.let {
                                settingViewModel.postUserDelete(loginId)
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red, // 버튼 배경 색상
                            contentColor = Color.LightGray // 버튼 텍스트 및 아이콘 색상
                        )
                    ) {
                        Text(text = "회원 탈퇴")
                    }
                }
            }
        }
    }
}
