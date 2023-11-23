package com.ssafy.booking.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.booking.viewmodel.HistoryViewModel

@Composable
fun RecordSummary(
) {
    val historyViewModel: HistoryViewModel = hiltViewModel()
    val summary by historyViewModel.SummaryInfo.observeAsState("")

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .background(Color.White)
    ) {
        SelectionContainer {
            Text(
                modifier = Modifier
                    .padding(20.dp),
                color = Color.Black,
                text = summary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Thin,
                lineHeight = 30.sp
            )
        }
    }
}
