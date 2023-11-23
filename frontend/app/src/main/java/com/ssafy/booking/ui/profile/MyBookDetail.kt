package com.ssafy.booking.ui.profile

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ssafy.booking.R
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.BackTopBar
import com.ssafy.booking.ui.common.HorizontalDivider
import com.ssafy.booking.viewmodel.MyBookViewModel
import com.ssafy.data.repository.token.TokenDataSource
import com.ssafy.domain.model.mybook.MyBookListResponse
import com.ssafy.domain.model.mybook.MyBookMemoRegisterRequest
import java.time.LocalDate
import java.time.LocalDate.now


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookDetail(
    isbn: String?,
    yourPk: Long
) {
    val navController = LocalNavigation.current
    val viewModel: MyBookViewModel = hiltViewModel()
    val context = LocalContext.current
    val tokenDataSource = TokenDataSource(context)
    val memberPk: Long = tokenDataSource.getMemberPk()

    val myBookDetailResponse by viewModel.myBookDetailResponse.observeAsState()

    var BookDetailState by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        if (memberPk != null && isbn != null) {
            viewModel.getMyBookDetailResponse(yourPk, isbn)
        }
    }

    LaunchedEffect(myBookDetailResponse) {
        myBookDetailResponse?.let {
            if (myBookDetailResponse!!.isSuccessful) {
                // 성공 view 띄우기
                BookDetailState = 1
            } else {
                // 실패 view 띄우기
                BookDetailState = 2
            }
        } ?: run {
            // null 에러 띄우기
            BookDetailState = 2
        }
    }

    Scaffold(
        topBar = {
            if(yourPk == memberPk) {
                CenterAlignedTopAppBar(
                    title = { Text(text = "도서") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "뒤로가기"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            myBookDetailResponse?.let {
                                Log.d("멤버북아이디", "${it.body()}")
                                if (it.isSuccessful) {
                                    it.body()?.let {
                                    Log.d("멤버북아이디", "${it.memberBookId}")
                                        viewModel.deleteBookRegister(it.memberBookId)
                                        navController.popBackStack()
                                    }
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "삭제"
                            )
                        }
                    }
                )
            } else {
                BackTopBar(title = "도서")
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (BookDetailState == 0) {
                Text(text = "로딩중..")
            } else if (BookDetailState == 1) {
                DetailBookSuccessView(myBookDetailResponse!!.body(), viewModel, yourPk, memberPk)
            } else {
                DetailBookErrorView()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBookSuccessView(
    myBookDetailResponse: MyBookListResponse?,
    viewModel: MyBookViewModel,
    yourPk: Long,
    memberPk: Long
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var memo by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .width(180.dp)
            .padding(10.dp)
    ) {
        // 북커버 이미지
        AsyncImage(
            model = myBookDetailResponse!!.bookInfo.coverImage,
            contentDescription = "북 커버",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
        )
    }
    Spacer(modifier = Modifier.size(15.dp))
    // 제목
    Text(text = myBookDetailResponse!!.bookInfo.title, fontSize = 20.sp)
    Spacer(modifier = Modifier.padding(10.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        // 저자
        Text(text = "저자", color = Color.Gray)
        Spacer(modifier = Modifier.size(5.dp))
        Text(text = myBookDetailResponse!!.bookInfo.author)
    }
    Spacer(modifier = Modifier.padding(5.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 장르
        Row(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "장르", color = Color.Gray)
            Spacer(modifier = Modifier.size(5.dp))
            Text(text = myBookDetailResponse!!.bookInfo.genre)
        }
        // 출간 연도
        Row (
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "발행", color = Color.Gray)
            Spacer(modifier = Modifier.size(5.dp))
            Text(text = myBookDetailResponse!!.bookInfo.publishDate)
        }
    }
    Spacer(modifier = Modifier.padding(15.dp))
    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
    Spacer(modifier = Modifier.padding(15.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "한줄평")
        Spacer(modifier = Modifier.padding(10.dp))
        OneLineMemos(myBookDetailResponse, yourPk, myBookDetailResponse.bookInfo.isbn, memberPk)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(memberPk == yourPk) {
                OutlinedTextField(
                    value = memo, // 이 부분을 뷰모델의 상태로 연결하거나 필요에 따라 변경
                    onValueChange = { newValue ->
                        memo = newValue
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .height(50.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF12BD7E)
                    ),
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        baselineShift = BaselineShift.None
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (memo != "") {
                                val result = MyBookMemoRegisterRequest(
                                    memberPk = yourPk,
                                    isbn = myBookDetailResponse.bookInfo.isbn,
                                    content = memo
                                )
                                viewModel.postBookMemo(result, now().toString())
                                memo = ""
                                keyboardController?.hide()
                            }
                        }
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            if (memo != "") {
                                val result = MyBookMemoRegisterRequest(
                                    memberPk = yourPk,
                                    isbn = myBookDetailResponse.bookInfo.isbn,
                                    content = memo
                                )
                                viewModel.postBookMemo(result, now().toString())
                                memo = ""
                                keyboardController?.hide()
                            }
                        }) {
                            Icon(Icons.Outlined.Send, contentDescription = null, tint = colorResource(
                                id = R.color.booking_1
                            ))
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}

@Composable
fun DetailBookErrorView() {
    Text(text = "에러가 발생했습니다.")
}

@Composable
fun OneLineMemos(
    myBookDetailResponse : MyBookListResponse,
    yourPk: Long,
    isbn: String?,
    memberPk: Long
) {
    val viewModel: MyBookViewModel = hiltViewModel()
    val notesList by viewModel.notesList.observeAsState(emptyList())

    LaunchedEffect(Unit, notesList, myBookDetailResponse) {
        isbn?.let {
            viewModel.getMyBookDetailResponse(yourPk,isbn)
        }
    }

    if(notesList != null) {
        notesList.forEachIndexed  {index, note->
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
//                    modifier = Modifier.fillMaxHeight(),
                ) {
                    Text("${note.createdAt.take(10)} : ")
                    Text(text = "${note.memo}")
                }
                if(memberPk == yourPk) {
                    IconButton(
                        onClick = {
                            viewModel.deleteBookNote(myBookDetailResponse.memberBookId, index)
                            viewModel.removeNoteAtIndex(index)
                        },
                        modifier = Modifier.size(15.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
        }
    }
}