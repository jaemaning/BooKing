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
import androidx.compose.ui.unit.sp
import com.ssafy.booking.R

@Composable
fun TabBarTwo(
    tabTitles: List<String>,
    contentForTab: @Composable (index: Int) -> Unit
) {
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
                        text = { Text(title, fontSize = 12.sp)}
                    )
                }
            }
        }
        // 선택된 탭에 따른 @Composable 렌더링
        contentForTab(selectedTabIndex)
    }
}
