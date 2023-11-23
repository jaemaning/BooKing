package com.ssafy.booking.ui.common

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.booking.R
import com.ssafy.booking.ui.LocalNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBar(
    title: String
) {
    val navController = LocalNavigation.current

    CenterAlignedTopAppBar(
        title = { Text(text = "$title", fontFamily = FontFamily(Font(R.font.gowundodum))) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "뒤로가기",
                    Modifier.size(30.dp)
                )
            }
        }
    )
}
