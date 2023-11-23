package com.ssafy.booking.ui.booking

import android.util.Log
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.ssafy.booking.R
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.HorizontalDivider
import com.ssafy.booking.ui.profile.MyBookFloatingActionButton
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.BookingBoardViewModel
import com.ssafy.domain.model.booking.BookingBoardReadListResponse
import com.ssafy.domain.model.booking.BookingParticipants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingBoard(meetingId : Long,
                 memberRole : String,
                 meetingState : String) {
    val viewModel : BookingBoardViewModel = hiltViewModel()
    val boardList = viewModel.getBookingBoardReadListResponse.observeAsState()
    val navController = LocalNavigation.current
    val context = LocalContext.current
    val imageLoader = context.imageLoader
    var sortedBoardList by remember { mutableStateOf<List<BookingBoardReadListResponse>>(emptyList()) }

    LaunchedEffect(Unit) {
        viewModel.getBookingBoardReadList(meetingId)
    }

    LaunchedEffect(boardList.value, sortedBoardList) {
        boardList?.value?.let {response->
            var sortByboardList = response.body()!!.sortedBy { it.postId }
            sortedBoardList = sortByboardList // 상태 업데이트
            Log.d("test444","$boardList, $sortedBoardList")
        }
        Log.d("test444","no")
    }

    if (memberRole == "LEADER" || memberRole == "PARTICIPANT") {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    Text(text = "N")
                    Spacer(modifier = Modifier.size(5.dp))
                    HorizontalDivider(thickness = 1.dp)
                    Spacer(modifier = Modifier.size(5.dp))
                    sortedBoardList?.let {
                        it.forEach { board->
                            Text(
                                "${board.postId}",
                                modifier = Modifier.clickable{
                                    navController.navigate("booking/board/detail/${board.postId}")
                                }
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(3f)
                ) {
                    Text(text = "제목")
                    Spacer(modifier = Modifier.size(5.dp))
                    HorizontalDivider(thickness = 1.dp)
                    Spacer(modifier = Modifier.size(5.dp))
                    sortedBoardList?.let {
                        it.forEach { board->
                            Text(
                                "${board.title}",
                                modifier = Modifier.clickable{
                                    navController.navigate("booking/board/detail/${board.postId}")
                                }
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2f)
                ) {
                    Text(text = "닉네임")
                    Spacer(modifier = Modifier.size(5.dp))
                    HorizontalDivider(thickness = 1.dp)
                    Spacer(modifier = Modifier.size(5.dp))
                    sortedBoardList?.let {
                        it.forEach { board->
                            Text(
                                "${board.nickname}",
                                modifier = Modifier.clickable{
                                    navController.navigate("profile/${board.memberId}") {
                                        popUpTo("booking/mybooking") { inclusive = true }
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2f)
                ) {
                    Text(text = "생성일")
                    Spacer(modifier = Modifier.size(5.dp))
                    HorizontalDivider(thickness = 1.dp)
                    Spacer(modifier = Modifier.size(5.dp))
                    sortedBoardList?.let {
                        it.forEach { board->
                            Text("${board.createdAt?.substring(2,10)}")
                            Spacer(modifier = Modifier.size(5.dp))
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { navController.navigate("booking/board/create/$meetingId") },
            modifier = Modifier
                .offset(x = 250.dp, y = 300.dp)
                .size(65.dp)
            ,
            containerColor = Color(0xFF12BD7E),
            shape = CircleShape
        ) {
            Icon(
                Icons.Outlined.Create,
                contentDescription = "Localized description",
                modifier = Modifier.size(40.dp),
                tint = Color.White

            )
        }

//            LazyVerticalGrid(
//                columns = GridCells.Fixed(3),
//                verticalArrangement = Arrangement.spacedBy(20.dp),
//                horizontalArrangement = Arrangement.spacedBy(10.dp),
//                modifier = Modifier
//                    .padding(paddingValues)
//                    .padding(30.dp),
//            ) {
//                boardList.value?.let {
//                    it.body()?.let {
//                        items(3) {idx ->
//                            if(idx%3 == 0) {
//                                Text(text = "글 제목")
//                            } else if (idx%3 == 1) {
//                                Text(text = "작성자")
//                            } else if (idx%3 == 2) {
//                                Text(text = "작성 날짜")
//                            }
//                        }
//                        items(it.size * 3) {idx->
//                            if(idx%3 == 0) {
//                                Text(
//                                    text = "${it[idx/3].title}",
//                                    modifier = Modifier.clickable{
//                                        navController.navigate("booking/board/detail/${it[idx/3].postId}")
//                                    }
//                                )
//                            } else if(idx%3 == 1) {
//                                Row(
//                                    modifier = Modifier.clickable{
//                                        navController.navigate("profile/${it[idx/3].memberId}")
//                                    },
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    AsyncImage(
//                                        model = ImageRequest.Builder(context)
//                                            .data("https://kr.object.ncloudstorage.com/booking-bucket/images/${it[idx/3].memberId}_profile.png")
//                                            .memoryCachePolicy(CachePolicy.DISABLED)
//                                            .addHeader("Host", "kr.object.ncloudstorage.com")
//                                            .crossfade(true)
//                                            .build(),
//                                        contentScale = ContentScale.Crop,
//                                        contentDescription = null,
//                                        imageLoader=imageLoader,
//                                        modifier = Modifier
//                                            .size(24.dp)
//                                            .clip(CircleShape),
//                                        error = painterResource(id = R.drawable.basic_profile)
//                                    )
//                                    Spacer(modifier = Modifier.size(8.dp))
//                                    Text(
//                                        text = "${it[idx/3].nickname}"
//                                    )
//                                }
//                            } else if(idx%3 == 2) {
//                                Text(
//                                    text = "${it[idx/3].createdAt?.substring(2, 10)}",
//                                    modifier = Modifier.clickable{
//                                        navController.navigate("booking/board/detail/${it[idx/3].postId}")
//                                    }
//                                )
//                            }
//                        }
//                    }
//                }
//            }
    } else {
        Column(
            modifier = Modifier
                .padding(30.dp)
                .fillMaxSize()
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "가입 신청 후 게시글을 볼 수 있습니다.")
        }
    }
}

@Composable
fun boardItem(board: BookingBoardReadListResponse){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "${board.postId}")
        Text(text = "${board.title}")
        Text(text = "${board.nickname}")
        Text(text = "${board.createdAt.take(10)}")
    }
}

@Composable
fun BoardCreateButton(
    meetingId: Long
) {
    val navController = LocalNavigation.current


}