package com.ssafy.booking.ui.common

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LiveData
import com.ssafy.domain.model.kakaopay.KakaoPayResponse
import retrofit2.Response

@Composable
fun WebViewScreen(kakaoPayLandingPageUrl: LiveData<Response<KakaoPayResponse>>) {
    val url = kakaoPayLandingPageUrl.value?.body()?.nextRedirectAppUrl

    // URL 상태가 변경되면 WebView를 업데이트합니다.
    if (url != null) {
        AndroidView(factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient() // 기본 WebViewClient 설정
                loadUrl(url)
            }
        }, update = { webView ->
            webView.loadUrl(url)
        })
    }
}
