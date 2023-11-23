package com.ssafy.booking.ui.booking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.booking.R
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.BackTopBar
import com.ssafy.booking.viewmodel.BookingBoardViewModel
import com.ssafy.domain.model.booking.BookingBoardCreateRequest


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BookingBoardCreate(
    meetingId: Long
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val viewModel : BookingBoardViewModel = hiltViewModel()
    val navController = LocalNavigation.current

    Scaffold(
        topBar = { BackTopBar(title = "게시글 생성") },
        modifier = Modifier.fillMaxSize()
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { newValue ->
                    title = newValue
                },
                label = { Text("제목") },
//                leadingIcon = { Icon(Icons.Filled.Add, contentDescription = "Money Icon") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(20.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { newValue ->
                    content = newValue
                },
                label = { Text("내용") },
//                leadingIcon = { Icon(Icons.Filled.Add, contentDescription = "Money Icon") },
                maxLines = 6, // 최대 6줄 입력 가능
                modifier = Modifier.fillMaxWidth()
                    .height(192.dp)
            )
            Spacer(modifier = Modifier.size(30.dp))
            Button(
                onClick = {
                val boardCreateRequest = BookingBoardCreateRequest(
                    meetingId = meetingId,
                    title = title,
                    content = content
                )
                viewModel.postBookingBoardCreate(boardCreateRequest)
                navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.booking_1),
                    contentColor = colorResource(id = R.color.font_color)
                )
            ) {
                Text(text = "게시글 작성")
            }
        }
    }
}