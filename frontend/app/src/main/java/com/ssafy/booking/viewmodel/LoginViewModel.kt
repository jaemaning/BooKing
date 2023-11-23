package com.ssafy.booking.viewmodel

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.ui.login.LoginInfo
import com.ssafy.booking.ui.login.TAG1
import com.ssafy.booking.ui.login.loginService
//import com.ssafy.booking.ui.login.onLoginSuccess
import com.ssafy.booking.utils.MyFirebaseMessagingService
import com.ssafy.data.repository.token.TokenDataSource
import com.ssafy.domain.model.google.AccountInfo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel : ViewModel() {

    fun loginCallback(
        context: Context,
        navController: NavController
    ): (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG1, "로그인 실패 $error")
        } else if (token != null) {
            Log.e(TAG1, "로그인 성공 ${token.accessToken}")
            // 사용자 정보 요청 (기본)
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e("asdf", "사용자 정보 요청 실패", error)
                } else if (user != null) {
                    val loginId = "kakao_" + user.id.toString()
                    onLoginSuccess(context, loginId, navController, user.kakaoAccount?.profile?.nickname.toString())
                    Log.i(
                        "loginInfo",
                        "사용자 정보 요청 성공" +
                                "\n회원번호: ${user.id}" +
                                "\n이메일: ${user.kakaoAccount?.email}" +
                                "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                    )
                }
            }
        }
    }

    fun googleLoginCallBack(
        context: Context,
        navController: NavController,
        accountInfo: AccountInfo
    ) {
        val loginId = "Google_" + accountInfo.loginId
        onLoginSuccess(context, loginId, navController, accountInfo.name)
    }

    fun onLoginSuccess(
        context: Context,
        loginId: String,
        navController: NavController,
        kakaoNickName: String
    ) {
        val loginInfo = LoginInfo(loginId = loginId) // 실제 로그인 ID로 변경해야 함
//    val loginInfo = LoginInfo(loginId = "kakao_3143286573")
        val call = loginService.login(loginInfo)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // 성공적으로 API 호출 완료, JWT 토큰 처리
                    val token = response.body()?.string()
                    Log.d("API", "API 호출 성공,AT는: $token")
                    val tokenDataSource = TokenDataSource(context)
                    // 토큰 넣기
                    tokenDataSource.putToken(token)
                    // 로그인 아이디 넣기
                    tokenDataSource.putLoginId(loginId)

                    val asdf = tokenDataSource.getLoginId()
                    Log.d("asdf", "로그인 아이디: $asdf")

                    MyFirebaseMessagingService.getFirebaseToken { token ->
                        val tokenDataSource = TokenDataSource(context)
                        tokenDataSource.putDeviceToken(token)
                    }

                    navController.navigate(AppNavItem.Main.route) {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                } else {
                    // 오류 처리
                    val errorCode = response.code()
                    Log.d("kakao", "$errorCode")
                    when (errorCode) {
                        // 에러 코드가 400이면 회원가입이 필요한 상태 -> 회원가입으로 라우트
                        400 -> {
                            navController.navigate(AppNavItem.SignIn.createRoute(loginId, kakaoNickName)) {
                                popUpTo("SignIn") { inclusive = false }
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // 네트워크 오류 등의 이유로 호출 실패
                Log.d(ContentValues.TAG, "API 호출 실패2: ${t.message}")
            }
        })
    }

    fun handleSignInResult(
        context: Context,
        accountTask: Task<GoogleSignInAccount>,
        viewModel: MainViewModel,
        firebaseAuth: FirebaseAuth
    ) {
        try {
            val account = accountTask.result ?: return
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            Log.d("구글로그인?", "$account, $credential")
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(context as Activity) { task ->
                    if (task.isSuccessful) {
                        Log.d("구글로그인?", "태스크 성공 / ${account.idToken.orEmpty()} / ${account.displayName.orEmpty()} / ${AccountInfo.Type.GOOGLE}")
                        viewModel.signInGoogle(
                            AccountInfo(
                                loginId = account.id.orEmpty(),
                                tokenId = account.idToken.orEmpty(),
                                name = account.displayName.orEmpty(),
                                type = AccountInfo.Type.GOOGLE
                            )
                        )
                    } else {
                        viewModel.signOutGoogle()
                        firebaseAuth.signOut()
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}