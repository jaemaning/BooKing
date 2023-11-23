package com.ssafy.booking.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.ssafy.booking.R
import com.ssafy.booking.di.App
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.BackTopBar
import com.ssafy.booking.ui.common.TabBar
import com.ssafy.booking.viewmodel.MyPageViewModel
import com.ssafy.domain.model.mypage.UserFollowersResponse
import com.ssafy.domain.model.mypage.UserFollowingsResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileFollowScreen(
    memberPk: Long
) {
    val viewModel: MyPageViewModel = hiltViewModel()
    val combinedData by viewModel.combinedUserFollowData.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getUserFollowers(memberPk)
        viewModel.getUserFollowings(memberPk)
    }

    Scaffold(
        topBar = { BackTopBar("Follow 목록") },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            combinedData?.let { (followers, followings) ->
                if (followers != null && followings != null) {
                    IsSuccessView(followings, followers)
                } else {
                    IsLoadingView()
                }
            }
        }
    }
}

@Composable
fun IsSuccessView(
    userFollowingsResponse: UserFollowingsResponse,
    userFollowersResponse: UserFollowersResponse
) {
    TabBar(
        tabTitles = listOf("팔로워 ${userFollowersResponse.followersCnt}", "팔로잉 ${userFollowingsResponse.followingsCnt}"),
        contentForTab = { index ->
            // 인덱스 마다 @composable 함수 넣으면 됨.
            when (index) {
                0 -> FollowerListScreen(userFollowersResponse)
                1 -> FollowingListScreen(userFollowingsResponse)
            }
        }
    )
}

@Composable
fun IsLoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("로딩중...")
    }
}

@Composable
fun FollowerListScreen(
    userFollowersResponse: UserFollowersResponse
) {
    val context = LocalContext.current
    val imageLoader = context.imageLoader
    val navController = LocalNavigation.current

    LazyColumn(
        modifier = Modifier.padding(24.dp)
            .fillMaxSize()
    ) {
        userFollowersResponse.followers?.let {followers->
            items(followers.size) { index->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clickable {
                            navController.navigate("profile/${followers[index]?.memberPk}")
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data("https://kr.object.ncloudstorage.com/booking-bucket/images/${followers[index]?.memberPk}_profile.png")
                            .memoryCachePolicy(CachePolicy.DISABLED)
                            .addHeader("Host", "kr.object.ncloudstorage.com")
                            .crossfade(true)
                            .build(),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        imageLoader=imageLoader,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),
                        error = painterResource(id = R.drawable.basic_profile)
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    Text("${followers[index]?.nickname}")
                }
            }
        }
    }
}

@Composable
fun FollowingListScreen(
    userFollowingsResponse: UserFollowingsResponse
) {
    val context = LocalContext.current
    val imageLoader = context.imageLoader
    val navController = LocalNavigation.current

    LazyColumn(
        modifier = Modifier.padding(24.dp)
            .fillMaxSize()
    ) {
        userFollowingsResponse.followings?.let {followings->
            items(followings.size) { index->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clickable {
                            navController.navigate("profile/${followings[index]?.memberPk}")
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data("https://kr.object.ncloudstorage.com/booking-bucket/images/${followings[index]?.memberPk}_profile.png")
                            .memoryCachePolicy(CachePolicy.DISABLED)
                            .addHeader("Host", "kr.object.ncloudstorage.com")
                            .crossfade(true)
                            .build(),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        imageLoader=imageLoader,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),
                        error = painterResource(id = R.drawable.basic_profile)
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    Text("${followings[index]?.nickname}")
                }
            }
        }
    }
}
