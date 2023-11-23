package com.ssafy.booking.ui.common

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.viewmodel.KakaoPayReadyViewModel
import com.ssafy.domain.model.kakaopay.KakaoPayRequest
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KakaoPayReadyScreen(
    firstAmount : String
) {
    var amount by remember { mutableStateOf(TextFieldValue(firstAmount)) }
    val viewModel : KakaoPayReadyViewModel = hiltViewModel()
    val KakaoPayLandingPageUrl = viewModel.kakaoPayResponse.observeAsState()
    val context = LocalContext.current
    val navController = LocalNavigation.current

    val isButtonEnabled = amount.text.isNotEmpty() && amount.text != "0"

    LaunchedEffect(KakaoPayLandingPageUrl.value) {
        Log.d("카카오페이", "$KakaoPayLandingPageUrl")
        KakaoPayLandingPageUrl.value?.let {
            Log.d("카카오페이", "${it.body()}")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.body()!!.nextRedirectMobileUrl))
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
                Log.d("카카오톡", "카카오톡 결제 페이지 연결")
            } else {
                // 처리할 앱이 없을 때 Google Play Store로 이동합니다.
                // 앱의 패키지 이름을 이용해 Google Play Store의 특정 페이지를 열 수 있습니다.
                val packageName = "com.kakao.talk" // 여기에 해당 앱의 패키지 이름을 넣으세요.
                val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                Log.d("카카오톡", "구글 플레이 스토어로 연결 준비")
                if (playStoreIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(playStoreIntent)
                    Log.d("카카오톡","구글 플레이 스토어로 연결")
                } else {
                    // Google Play Store도 사용할 수 없는 경우의 대비책을 추가합니다.
                    Log.d("카카오톡","구글 플레이 스토어를 설치하세요")
                }
            }
            delay(3000)
            navController.popBackStack()
        }

    }

    Scaffold(
        topBar = { BackTopBar(title = "카카오페이 결제 페이지") },
//        containerColor = Color(0xFF12BD7E)
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "충전 금액")
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = amount,
                onValueChange = { newAmount->
                    amount = newAmount
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("충전 금액") },
                maxLines = 1, // 최대 6줄 입력 가능
//                modifier = Modifier.height(192.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    val kakaoPayRequest = KakaoPayRequest(
                        amount = amount.text
                    )
                    viewModel.getKakaoPayPage(kakaoPayRequest)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECE330)), // 카카오색 설정
                shape = RoundedCornerShape(4.dp),
                enabled = isButtonEnabled
            ) {
                Text(text = "카카오페이로 충전하기",color = Color.Black)
            }
        }
    }
}