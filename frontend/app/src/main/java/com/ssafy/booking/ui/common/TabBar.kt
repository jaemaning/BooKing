package com.ssafy.booking.ui.common
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.booking.R

@Composable
fun TabBar(
    tabTitles: List<String>, // 탭의 제목을 입력받는 곳.
    contentForTab: @Composable (index: Int) -> Unit // 각 탭의 내용을 렌더링하기 위한 @Composable 함수 받기
) {
    // remember는 Jetpack Compose에서 제공하는 함수, 컴포넌트가 다시 렌더링 될 때마다 상태 초기화 방지.
    var selectedTabIndex by remember { mutableStateOf(0) }
    Column {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            TabRow(
                modifier = Modifier.fillMaxWidth(0.85f), // 80% of parent width
                selectedTabIndex = selectedTabIndex,
                containerColor = colorResource(id = R.color.background_color),
                contentColor = colorResource(id = R.color.font_color),

                // indicator가 탭바 밑의 색깔
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = Color(0xFF00C68E),
                        height = 4.dp
                    )
                },
                divider = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(4.dp)
                            .background(Color(0xFFD8D8D8))
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        // 선택된 탭이면 selected를 true로 선택
                        selected = selectedTabIndex == index,
                        // 클릭하면 selectedTabIndex 상태를 현재 탭의 index로 업데이트
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }
        }
        // 선택된 탭에 따른 @Composable 렌더링
        contentForTab(selectedTabIndex)
    }
}

// 테스트용 Preview
@Preview(showBackground = true)
@Composable
fun PreviewMyTabBar() {
    TabBar(
        tabTitles = listOf("모임 정보", "참가자"),
        contentForTab = { index ->
            when (index) {
                0 -> Text("모임 정보 내용")
                1 -> Text("참가자 내용")
                2 -> Text("게시판 내용")
                3 -> Text("테스트")
                // 필요에 따라 추가/수정 가능
            }
        }
    )
}
