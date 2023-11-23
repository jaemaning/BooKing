//package com.ssafy.booking.ui.booking.bookingSetting
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.ArrowDropDown
//import androidx.compose.material3.Button
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Preview
//@Composable
//fun SetEntryFee() {
//    Scaffold(
//        bottomBar = { (SetEntryFeeBottomButton()) }
//    ) { innerPadding ->
//        setEntryFee(Modifier.padding(innerPadding))
//    }
//}
//
//@Preview
//@Composable
//fun setEntryFee(modifier: Modifier = Modifier) {
//    var enteredFee by remember { mutableStateOf(0) }
//
//    Column(modifier = modifier) {
//        FeeInputField(onFeeChanged = { fee ->
//            enteredFee = fee
//        })
//        Text("설정된 참가비: $enteredFee 원")
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun FeeInputField(onFeeChanged: (Int) -> Unit) {
//    var fee by remember { mutableStateOf(0) }
//
//    Column(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        OutlinedTextField(
//            value = fee.toString(),
//            onValueChange = { newValue ->
//                fee = newValue.toIntOrNull() ?: 0
//                onFeeChanged(fee)
//            },
//            label = { Text("참가비 입력") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            leadingIcon = { Icon(Icons.Filled.Add, contentDescription = "Money Icon") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Row(
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Button(onClick = { fee += 100; onFeeChanged(fee) }) { Text("+100원") }
//            Button(onClick = { fee += 1000; onFeeChanged(fee) }) { Text("+1,000원") }
//            Button(onClick = { fee += 10000; onFeeChanged(fee) }) { Text("+10,000원") }
//            Button(onClick = { fee += 20000; onFeeChanged(fee) }) { Text("+20,000원") }
//        }
//    }
//}
//
//@Composable
//fun SetEntryFeeBottomButton() {
//    Box(
//        contentAlignment = Alignment.BottomCenter,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        Button(
//            onClick = { /* 버튼 클릭 시 실행할 동작 */ },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(48.dp),
//            shape = RoundedCornerShape(3.dp) // 사각형 모양
//        ) {
//            Text("버튼 텍스트", style = MaterialTheme.typography.bodyMedium)
//        }
//    }
//}