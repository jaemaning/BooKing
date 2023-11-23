package com.ssafy.booking.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ssafy.booking.R
import com.ssafy.booking.model.MyBookState
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.domain.model.mybook.MyBookListResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBook(
    myBookState: MyBookState?,
    data: ProfileData
) {
    Scaffold(
        floatingActionButton = {
            if(data.isI) {
                MyBookFloatingActionButton()
            }
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(32.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            // myBookState 값에 따라 UI를 조건부로 렌더링
            when (myBookState) {
                is MyBookState.Loading -> Text("정보를 불러오는 중...")
                is MyBookState.Success -> MyBookListView(books = (myBookState as MyBookState.Success).data, yourPk = data.myProfile!!.memberPk)
                is MyBookState.Error -> MyBookErrorView(message = (myBookState as MyBookState.Error).message)
                else -> Text("정보를 불러오는 중...")
            }
        }
    }
}

@Composable
fun MyBookFloatingActionButton() {
    val navController = LocalNavigation.current

    FloatingActionButton(
        onClick = {
                  navController.navigate("profile/book/gg")
        },
        modifier = Modifier
            .padding(end = 16.dp, bottom = 10.dp)
            .size(65.dp),
        containerColor = colorResource(id = R.color.booking_1),
        shape = CircleShape
        // 그냥 동그라미할지, + 모임생성할지 고민.
    ) {
//        Icon(
//            Icons.Filled.Add,
//            contentDescription = "Localized description",
//            modifier = Modifier.size(40.dp),
//            tint = Color.White
//        )
        Icon(
            painter = painterResource(id = R.drawable.outline_book_24),
            contentDescription = "Localized description",
            modifier = Modifier.size(40.dp),
            tint = Color.White
        )
    }
}


@Composable
fun MyBookListView(
    books : List<MyBookListResponse>,
    yourPk : Long
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 두 컬럼으로 설정
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        items(books.size) { index ->
            MyBookItem(book = books[index], yourPk = yourPk)
        }
    }
}

@Composable
fun MyBookItem(
    book : MyBookListResponse,
    yourPk : Long
) {
    val navController = LocalNavigation.current

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("profile/book/detail/${book.bookInfo.isbn}/$yourPk")
            },
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.background_color)
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = book.bookInfo.coverImage,
                contentDescription = "책 커버 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(170.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                book.bookInfo.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun MyBookErrorView(
    message : String
) {
    Text(text = "에러 발생 : $message")
}