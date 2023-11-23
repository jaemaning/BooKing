package com.ssafy.booking.ui.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.ssafy.booking.R
import com.ssafy.booking.model.UserInfoChangeResult
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.BackTopBar
import com.ssafy.booking.viewmodel.MyPageViewModel
import com.ssafy.data.repository.token.TokenDataSource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileModifierScreen() {
    val navController = LocalNavigation.current
    val imageLoader = LocalContext.current.imageLoader

    // 닉네임, 프로필이미지 가져오기
    val context = LocalContext.current
    val tokenDataSource = TokenDataSource(context)
    val nickname: String? = tokenDataSource.getNickName()
    val profileImg: String? = tokenDataSource.getProfileImage()
    val loginId: String? = tokenDataSource.getLoginId()
    val memberPk: Long = tokenDataSource.getMemberPk()

    var nick by remember { mutableStateOf("$nickname") }
    var pImg by remember { mutableStateOf("$profileImg") }
    var isError by remember { mutableStateOf(false) }
    var isErrorNick by remember { mutableStateOf(false) }

    var imageUri by remember { mutableStateOf<RequestBody?>(null) }
    var galleryUri by remember { mutableStateOf<Uri?>(null) }

    val viewModel : MyPageViewModel = hiltViewModel()
    val naverResult by viewModel.naverCloudPutResponse.observeAsState()

//    LaunchedEffect(Unit) {
//        memberPk?.let {
//            viewModel.GetToNaverCloud(memberPk)
//        }
//    }

//    LaunchedEffect(Unit) {
//        Log.d("test", "$nick, $pImg, $nickname")
//    }
//
//    LaunchedEffect(naverResult) {
//        naverResult?.let {
//            val errorBodyString = it.errorBody()?.string()
//            if (it.isSuccessful) {
//                Log.d("naver", "성공: ${it.body()}")
//            } else {
//                Log.d("naver", "오류: $errorBodyString")
//                // 오류 메시지를 다른 곳에서 사용할 수 있습니다.
//            }
//        }
//    }

//    val imageTest by viewModel.naverCloudGetResponse.observeAsState()
//    var bitmap : Bitmap? by remember { mutableStateOf(null) }
//
//    LaunchedEffect(imageTest) {
//        imageTest?.let{
//            val inputStream = imageTest!!.body()?.byteStream()
//            bitmap = BitmapFactory.decodeStream(inputStream)
//        }
//    }

    // 갤러리 접근
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            galleryUri = uri
            val contentResolver = context.contentResolver

            val inputStream = contentResolver.openInputStream(uri)
            imageUri = inputStream?.readBytes()?.toRequestBody("image/jpeg".toMediaTypeOrNull())
        }
    }

    // 뷰모델 연결
    val myPageViewModel: MyPageViewModel = hiltViewModel()
    val patchUserInfoResponse by myPageViewModel.patchUserInfoResponse.observeAsState()

    // UI 컴포넌트 (예: Fragment) 내부
    myPageViewModel.userInfoChangeResult.collectAsState().value?.let { result ->
        when (result) {
            is UserInfoChangeResult.Success -> {
                // 성공 상태 처리
                tokenDataSource.putNickName(result.nick)
                tokenDataSource.putProfileImage(result.pImg)
                navController.navigate(result.destination)
            }
            is UserInfoChangeResult.Error -> {
                // 실패 상태 처리
                if (result.isErrorNick) {
                    // 중복 닉네임 에러 처리
                    isErrorNick = true
                } else {
                    Log.d("resultError", "알 수 없는 에러 발생")
                }
            }
            else -> {
                Log.d("resultError","result error 발생")
            }
        }
    }

    Scaffold(
        topBar = { BackTopBar("회원 정보 수정") },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )

            // 프로필 이미지 변경
            // 갤러리에서 이미지를 선택 했다면 -> 그 이미지를 보여주기
            // 갤러리에서 이미지를 선택하지 않았다면 -> 기존 이미지 보여주기 or 기존 이미지 없다면 basic image 로 보여주기
            galleryUri?.let {uri->
                Image(
                    painter = rememberAsyncImagePainter(model = uri),
                    contentDescription = "선택된 이미지",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, RoundedCornerShape(20.dp))
                        .clickable { launcher.launch("image/*") }
                )
            } ?: run {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("https://kr.object.ncloudstorage.com/booking-bucket/images/${memberPk}_profile.png")
                        .memoryCachePolicy(CachePolicy.DISABLED)
                        .addHeader("Host", "kr.object.ncloudstorage.com")
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    imageLoader=imageLoader,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, RoundedCornerShape(20.dp))
                        .clickable { launcher.launch("image/*") },
                    error = painterResource(id = R.drawable.basic_profile)
                )
            }

            Spacer(modifier = Modifier.padding(24.dp))

            // 닉네임 변경
            if (isErrorNick) {
                Text("중복된 닉네임 입니다.", color = Color.Red)
            }
            OutlinedTextField(
                value = nick,
                onValueChange = {
                    nick = it
                    isError = it.isEmpty()
                    isErrorNick = false
                },
                label = { Text("*닉네임") },
                singleLine = true,
                isError = isError
            )

            Spacer(modifier = Modifier.padding(24.dp))

            // 회원 정보 수정 완료 버튼
            imageUri?.let{
                Button(
                    modifier = Modifier.width(280.dp),
                    // 정보 수정 api 요청
                    // 정보 수정 후 sharedPreference 정보 변경
                    // 이후 profile 화면으로 이동 시키기
    //                onClick = { myPageViewModel.userInfoChange(nick, pImg, loginId!!, memberPk) },
                    onClick = { myPageViewModel.userInfoChange(nick, loginId!!, memberPk, imageUri!!) },
                    enabled = !isError
                ) {
                    Text(text = "회원 정보 수정")
                }
            } ?: run {
                Button(
                    modifier = Modifier.width(280.dp),
                    // 정보 수정 api 요청
                    // 정보 수정 후 sharedPreference 정보 변경
                    // 이후 profile 화면으로 이동 시키기
                    //                onClick = { myPageViewModel.userInfoChange(nick, pImg, loginId!!, memberPk) },
                    onClick = { myPageViewModel.userInfoChange(nick, loginId!!, memberPk, null) },
                    enabled = !isError
                ) {
                    Text(text = "회원 정보 수정")
                }
            }
        }
    }
}
