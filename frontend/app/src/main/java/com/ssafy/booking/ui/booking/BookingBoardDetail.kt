package com.ssafy.booking.ui.booking

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.ssafy.booking.R
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.viewmodel.BookingBoardViewModel
import com.ssafy.data.repository.token.TokenDataSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingBoardDetail(
    postId : Long
) {
    val viewModel : BookingBoardViewModel = hiltViewModel()
    val boardData = viewModel.getBookingBoardReadDetailResponse.observeAsState()
    val navController = LocalNavigation.current
    val context = LocalContext.current
    val imageLoader = context.imageLoader
    val tokenDataSource = TokenDataSource(context)
    val memberPk : Long = tokenDataSource.getMemberPk()

    LaunchedEffect(Unit) {
        viewModel.getBookingBoardReadDetail(postId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "게시글 상세") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            boardData.value?.let {
                if (it.isSuccessful) {
                    it.body()?.let {boardInfo->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Row(
                                        modifier = Modifier.clickable{
                                            navController.navigate("profile/${boardInfo.memberId}") {
                                                popUpTo("booking/mybooking") { inclusive = true }
                                            }
                                        }
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(context)
                                                .data("https://kr.object.ncloudstorage.com/booking-bucket/images/${boardInfo.memberId}_profile.png")
                                                .memoryCachePolicy(CachePolicy.DISABLED)
                                                .addHeader("Host", "kr.object.ncloudstorage.com")
                                                .crossfade(true)
                                                .build(),
                                            contentScale = ContentScale.Crop,
                                            contentDescription = null,
                                            imageLoader=imageLoader,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape),
                                            error = painterResource(id = R.drawable.basic_profile)
                                        )
                                        Spacer(modifier = Modifier.size(15.dp))
                                        Text(text = boardInfo.nickname)
                                    }
                                    Spacer(modifier = Modifier.size(15.dp))
                                    Text(text = "| ${boardInfo.createdAt}")
                                }
                                if(memberPk == boardInfo.memberId.toLong()) {
                                    IconButton(onClick = {
                                        viewModel.deleteBookingBoardDelete(postId)
                                        navController.popBackStack()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Clear,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.size(40.dp))
                            Text(
                                text = "${boardInfo.title}",
                                fontSize = 30.sp
                            )
                        }
                        Column(

                        ) {
                            Spacer(modifier = Modifier.size(30.dp))
                            Text(text = "내용", fontSize = 30.sp)
                            Spacer(modifier = Modifier.size(15.dp))
                            Text(text = boardInfo.content)
                        }
                    }
                }
            }
        }
    }
}