package com.ssafy.booking.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.BottomAppBarDefaults.containerColor
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ssafy.booking.R
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.data.repository.token.TokenDataSource

enum class NavItem(val route: String, val title: String) {
    Book("book", "book"),
    History("history", "History"),
    Main("main", "Home"),
    Chat("chat", "Chat"),
    Profile("profile", "Profile")
}

@Composable
fun BottomNav(
    navController: NavController,
    appViewModel: AppViewModel
) {
    // 현재 백 스택 엔트리를 관찰
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // 현재 라우트를 결정
    val currentRoute = navBackStackEntry?.destination?.route
    // 아이템 목록을 정의
    val items = listOf(
        AppNavItem.Book,
        AppNavItem.MyBooking,
        AppNavItem.Main,
        AppNavItem.Chat,
        AppNavItem.Profile
    )
    // 선택된 아이템과 선택되지 않은 아이템의 색상을 정의
    val selectedColor = Color(0xFF00C68E)
    val unselectedColor = colorResource(id = R.color.font_color)
    val context = LocalContext.current
    val tokenDataSource = TokenDataSource(context)
    val memberPk : Long = tokenDataSource.getMemberPk()

    NavigationBar(containerColor = colorResource(id = R.color.background_color)) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    val painter = when (item) {
                        AppNavItem.Book -> painterResource(R.drawable.baseline_menu_book_24)
                        AppNavItem.MyBooking -> painterResource(R.drawable.baseline_groups_24)
                        AppNavItem.Main -> painterResource(R.drawable.baseline_home_24)
                        AppNavItem.Chat -> painterResource(R.drawable.baseline_message_24)
                        AppNavItem.Profile -> painterResource(R.drawable.baseline_account_circle_24)
                        else -> {
                            painterResource(R.drawable.main1)
                        }
                    }
                    Icon(painter, contentDescription = null)
                },
//                painter = painterResource(R.drawable.timeline_fill0_wght400_grad0_opsz24)
                label = {
                    val label = when (item) {
                        AppNavItem.Book -> "도서"
                        AppNavItem.MyBooking -> "나의 북킹"
                        AppNavItem.Main -> "홈"
                        AppNavItem.Chat -> "채팅"
                        AppNavItem.Profile -> "프로필"
                        else -> {
                            "기타"
                        }
                    }
                    Text(label)
                },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        val route = when (item) {
                            AppNavItem.Book -> "book/0"
                            AppNavItem.Profile -> "profile/$memberPk"
                            else -> item.route
                        }

                        navController.navigate(route) {
                            launchSingleTop = true // 현재 탑 레벨 데스티네이션을 재시작하지 않음.
                            restoreState = true // 상태를 복원합니다 (예: 스크롤 위치).
                            popUpTo(AppNavItem.Main.route) {
                                saveState = true // 데스티네이션을 pop할 때 상태를 저장
                            }
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = colorResource(id = R.color.second_background_color), // 인디케이터 색상을 투명하게 설정합니다.
                    selectedIconColor = selectedColor,
                    unselectedIconColor = unselectedColor,
                    selectedTextColor = selectedColor,
                    unselectedTextColor = unselectedColor
                )
            )
        }
    }
}

