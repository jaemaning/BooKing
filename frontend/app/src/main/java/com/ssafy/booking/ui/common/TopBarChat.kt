package com.ssafy.booking.ui.common

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ssafy.booking.ui.LocalNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarChat(
    title: String,
    onNavigationIconClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val navController = LocalNavigation.current

    TopAppBar(
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color(0xFF00C68E)
        ),
        title = {
            Text(text="${title}", color= Color.White)
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "뒤로가기",
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(Icons.Filled.Menu, contentDescription = "Open Drawer", tint = Color.White)
            }
        }
    )
}
