package com.ssafy.booking.ui.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.Navigation.findNavController
import coil.compose.AsyncImage
import com.ssafy.booking.R
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.viewmodel.BookSearchViewModel
import com.ssafy.booking.viewmodel.MyBookViewModel
import com.ssafy.data.repository.token.TokenDataSource
import com.ssafy.domain.model.mybook.MyBookMemoRegisterRequest
import com.ssafy.domain.model.mybook.MyBookRegisterRequest
import kotlinx.coroutines.delay
import java.time.LocalDate.now


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookRegister(
    isbn : String?
) {
    val navController = LocalNavigation.current

    Scaffold(
        topBar = { CenterAlignedTopAppBar(
            title = { Text(text = "서재 등록") },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "뒤로가기"
                    )
                }
            }
        )}
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CreateMyBook(isbn)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMyBook(
    isbn : String?
) {
    val navController = LocalNavigation.current
    val context = LocalContext.current
    val tokenDataSource = TokenDataSource(context)
    val memberPk: Long = tokenDataSource.getMemberPk()

    val viewModel : MyBookViewModel = hiltViewModel()

    val bookSearchViewModel: BookSearchViewModel = hiltViewModel()
    val getBookSearchByIsbnResponse by bookSearchViewModel.getBookSearchByIsbnResponse.observeAsState()
    val postBookRegisterResult by viewModel.postBookRegisterResult.observeAsState()

    var memo by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        isbn?.let {
            bookSearchViewModel.getBookSearchByIsbn(isbn)
        }
        Log.d("test9999","$postBookRegisterResult")
    }

    LaunchedEffect(postBookRegisterResult) {
        postBookRegisterResult?.let {
            response ->
            if(response.isSuccessful) {
                navController.navigate("profile/$memberPk")
            } else {
                navController.navigate("profile/$memberPk")
                Toast.makeText(context, "이미 서재에 등록된 도서입니다.", Toast.LENGTH_LONG).show()
            }
        }
    }


    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Color.LightGray)
                .width(150.dp)
                .height(210.dp)
                .clickable {
                    navController.navigate("book/2")
                }
        ) {
            // 왼쪽의 도서 등록 칸
            getBookSearchByIsbnResponse?.body()?.let {
                val book = it
                AsyncImage(
                    model = book!!.coverImage,
                    contentDescription = "북 커버",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            } ?: run {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(80.dp)
                )
            }
        }
        getBookSearchByIsbnResponse?.body()?.let {
            val book = it
            Text(
                text = book.title,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = book.author,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(25.dp))
            // 메모 등록하는것
            Text(text = "한줄 평 (선택 사항)")
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = memo,
                onValueChange = { it -> memo = it},
                placeholder = { Text("한줄 평") },
                maxLines = 6, // 최대 6줄 입력 가능
                modifier = Modifier.height(192.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )

            // 버튼
            memberPk?.let {
                Button(
                    onClick={
                        val bookRegisterInfo = MyBookRegisterRequest(
                            memberPk = memberPk,
                            bookIsbn = book.isbn,
                        )
                        viewModel.postBookRegister(bookRegisterInfo)
                        if (memo != "") {
                            val memoType = MyBookMemoRegisterRequest(
                                memberPk = memberPk,
                                isbn= book.isbn,
                                content = memo
                            )
                            viewModel.postBookMemo(memoType, now().toString())
                        }
                    },
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text("내 서재에 등록")
                }
            } ?: run {
                Text("로그인을 먼저 해주세요.")
            }
        }

    }
}