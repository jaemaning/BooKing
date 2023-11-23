package com.ssafy.booking.ui.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.ssafy.booking.R
import com.ssafy.booking.di.App
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.TopBarChat
import com.ssafy.booking.viewmodel.ChatViewModel
import com.ssafy.booking.viewmodel.MyPageViewModel
import com.ssafy.booking.viewmodel.SocketViewModel
import com.ssafy.data.room.entity.MessageEntity
import com.ssafy.domain.model.ChatExitRequest
import com.ssafy.domain.model.KafkaMessage
import com.ssafy.domain.model.mypage.UserInfoResponseByPk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetail(
    navController: NavController,
    socketViewModel: SocketViewModel,
    chatId: String?,
    memberListString: String?,
    meetingTitle: String?
) {
    val imageLoader = LocalContext.current.imageLoader
    val context = LocalContext.current
    val chatViewModel: ChatViewModel = hiltViewModel()
    val myPageViewModel: MyPageViewModel = hiltViewModel()
    val socketViewModel: SocketViewModel = hiltViewModel()
    val chatId = chatId ?: return
    val meetingTitle = meetingTitle ?: return
    val memberList = memberListString?.split(",")?.map { it.toInt() } ?: return

    val userInfoMap by socketViewModel.userInfoMap.observeAsState(emptyMap())
    val messages by socketViewModel.finalMessages.observeAsState(initial = emptyList())

    // UI 상태
    var isLoading by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
//        이전 메시지를 보고 있을 때는 갱신 안함
        if(listState.firstVisibleItemScrollOffset >= 500) {
        } else {
        socketViewModel.loadAllMessage(chatId.toInt())
        isLoading = false
        }
    }
//    리스트를 다시 내리면 갱신
    LaunchedEffect(listState.firstVisibleItemScrollOffset){
        if(listState.firstVisibleItemScrollOffset <= 10) {
            socketViewModel.loadAllMessage(chatId.toInt())
        }
    }

// 소켓 연결 + 읽었다고 보내기
    chatId.let {
        LaunchedEffect(Unit) {
            socketViewModel.connectToChat(chatId)
            socketViewModel.postLastReadMessageId(chatId.toInt())
        }
    }
// 유저 정보 매핑
    LaunchedEffect(messages) {
        memberList.forEach { memberId ->
            socketViewModel.loadUserInfo(memberId.toLong())
        }
    }
// 로딩 후 첫 스크롤
    LaunchedEffect(isLoading, messages) {
        if (!isLoading && messages.isNotEmpty()) {
            coroutineScope.launch {
                if (messages.isNotEmpty()) {
                    listState.animateScrollToItem(Int.MAX_VALUE)
                    isLoading = true
                }
            }
        }
    }
// 무한 스크롤
//    LaunchedEffect(listState.firstVisibleItemScrollOffset) {
//        if(!isLoading) {delay(2000)}
//        Log.d("TEST", "무한 스크롤 조건 ${listState.firstVisibleItemIndex} , ${listState.firstVisibleItemScrollOffset}")
//        if (listState.firstVisibleItemScrollOffset <= 0 && listState.firstVisibleItemIndex <= 0) {
//            socketViewModel.loadMoreMessages(chatId.toInt())
//            delay(300)
//            socketViewModel.loadMoreMessages(chatId.toInt())
//            delay(300)
//            listState.scrollToItem(listState.firstVisibleItemIndex + 1)
//            socketViewModel.loadMoreMessages(chatId.toInt())
//            delay(300)
//            socketViewModel.loadMoreMessages(chatId.toInt())
//            delay(300)
//            listState.scrollToItem(listState.firstVisibleItemIndex + 1)
//        }
//    }


// 맨 밑 스크롤 유지
    LaunchedEffect(messages) {
        delay(100)
        if (listState.isScrolledToTheBottom()) {
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(Int.MAX_VALUE)
            }
        }
    }

// 자신의 정보 불러오기
    var memberId by remember { mutableStateOf<Long?>(null) }
    var nickname by remember { mutableStateOf("") }
    val loginId = App.prefs.getLoginId()
    val getUserInfoResponse by myPageViewModel.getUserInfoResponse.observeAsState()
    LaunchedEffect(loginId) {
        loginId?.let {
            myPageViewModel.getUserInfo(loginId)
        }
    }
    getUserInfoResponse?.let { response ->
        memberId = response.body()?.memberPk
        nickname = response.body()?.nickname ?: ""
    }
// 나갈 때 소켓 연결 해제
    DisposableEffect(chatId) {
        onDispose {
            if (chatId != null) {
                socketViewModel.disconnectChat(chatId)
            }
        }
    }

// 무한스크롤을 위한 역순
    val reversedMessages = messages.reversed()

    ModalNavigationDrawer(
        gesturesEnabled = !drawerState.isClosed,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        modifier = Modifier.padding(20.dp),
                        text = "${meetingTitle}"
                    )
                    Divider()
                    Spacer(Modifier.height(12.dp))
                    userInfoMap.forEach { user ->
                        NavigationDrawerItem(
                            icon = {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data("https://kr.object.ncloudstorage.com/booking-bucket/images/${user.value.memberPk}_profile.png")
                                        .memoryCachePolicy(CachePolicy.DISABLED)
                                        .addHeader("Host", "kr.object.ncloudstorage.com")
                                        .crossfade(true)
                                        .build(),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null,
                                    imageLoader=imageLoader,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            if (user.value?.memberPk != null) {
                                                navController.navigate("profile/${user.value.memberPk}")
                                            }
                                        },
                                    error = painterResource(id = R.drawable.basic_profile)
                                )
                            },
                            label = { Text(user.value.nickname) },
                            selected = false,
                            onClick = {
                                if (user.value.memberPk != null) {
                                    navController.navigate("profile/${user.value.memberPk}")
                                }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Divider()
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Filled.ExitToApp, "채팅방 나가기") },
                        label = { Text("채팅방 나가기") },
                        selected = false,
                        onClick = {
                            val request = ChatExitRequest(chatId, memberId)
                            chatViewModel.exitChatRoom(request)
                            navController.popBackStack()
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopBarChat(
                    title = "${meetingTitle}",
                    onNavigationIconClick = {
                        coroutineScope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            contentWindowInsets = ScaffoldDefaults
                .contentWindowInsets
                .exclude(WindowInsets.navigationBars)
                .exclude(WindowInsets.ime),
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { paddingValues ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                MessageList(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFF00C68E)),
                    listState,
                    reversedMessages,
                    memberId,
                    userInfoMap,
                    chatId
                )
                chatId.let {
                    InputText(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .imePadding(),
                        socketViewModel,
                        listState,
                        messages,
                        chatId,
                        memberId,
                        nickname,
                        coroutineScope,
                    )
                }
            }
        }
    }
}

// 맨 밑 탐지
fun LazyListState.isScrolledToTheBottom(): Boolean {
    if (layoutInfo.visibleItemsInfo.lastOrNull()?.index != null) {
        return layoutInfo.visibleItemsInfo.lastOrNull()?.index!! >= layoutInfo.totalItemsCount - 5
    } else {
        return false
    }
}

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    messages: List<MessageEntity>,
    memberId: Long?,
    userInfoMap: Map<Long, UserInfoResponseByPk>,
    chatId: String?
) {

    Box(
        modifier = modifier
            .background(Color(0xFF00C68E))
            .fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            modifier = modifier
                .padding(16.dp)
        ) {
            itemsIndexed(messages) { index, message ->
                val previousMessage = if (index > 0) messages[index - 1] else null
                val nextMessage =
                    if (index < messages.size - 1) messages[index + 1] else null
                MessageItem(message, previousMessage, nextMessage, memberId, userInfoMap, chatId)
            }
        }
    }
}

@Composable
fun MessageItem(
    message: MessageEntity,
    previousMessage: MessageEntity?,
    nextMessage: MessageEntity?,
    memberId: Long?,
    userInfoMap: Map<Long, UserInfoResponseByPk>,
    chatId: String?
) {
    val imageLoader = LocalContext.current.imageLoader
    val context = LocalContext.current
    val navController = LocalNavigation.current
    val isOwnMessage = message.senderId?.toLong() == memberId
    val userInfo = userInfoMap[message.senderId?.toLong()]

    val nextTime = nextMessage?.timeStamp?.let { formatTimestamp(it) }
    val curTime = formatTimestamp(message.timeStamp)
    val prevTime = previousMessage?.timeStamp?.let { formatTimestamp(it) }

    val prevDate = previousMessage?.timeStamp?.let { formatDate(it) }
    val curDate = formatDate(message.timeStamp)

    // 날짜 표시
    if (prevDate == null || curDate != prevDate) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = curDate,
                modifier = Modifier
                    .background(
                        color = Color(0xFF00AD97),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(8.dp)
                    .widthIn(max = 150.dp),
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (isOwnMessage) 6.dp else 0.dp),
            horizontalArrangement = if (isOwnMessage) Arrangement.End else Arrangement.Start

        ) {
            if (isOwnMessage) {
                // 자신의 메시지
            } else if (previousMessage?.senderId != message.senderId) {
                // 타인의 메시지
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("https://kr.object.ncloudstorage.com/booking-bucket/images/${message.senderId}_profile.png")
                        .memoryCachePolicy(CachePolicy.DISABLED)
                        .addHeader("Host", "kr.object.ncloudstorage.com")
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    imageLoader=imageLoader,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
//                        .border(1.dp, Color.Gray, RoundedCornerShape(20.dp))
                        .clickable {
                            if (userInfo?.memberPk != null) {
                                navController.navigate("profile/${userInfo.memberPk}")
                            }
                        },
                        error = painterResource(id = R.drawable.basic_profile)
                )
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.width(4.dp))

            Column {
                // 이름을 표시하는 조건
                if (!isOwnMessage && previousMessage?.senderId != message.senderId) {
                    Text(
                        text = "${userInfo?.nickname ?: "알 수 없음"}"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Row(
                    horizontalArrangement = if (isOwnMessage) Arrangement.End else Arrangement.Start,
                    modifier = Modifier.fillMaxWidth() // 이 부분을 추가하여 전체 너비를 사용하도록 설정
                ) {
                    // 자신의 메시지인 경우, 시간을 먼저 표시
                    if (isOwnMessage &&
                        (nextMessage == null || nextTime != curTime || nextMessage.senderId != message.senderId)
                    ) {
                        Column(modifier = Modifier.align(Alignment.Bottom)) {
                            Row(modifier = Modifier.align(Alignment.End)) {
                                if (message.readCount!! > 0) {
                                    Text(
                                        text = "${message.readCount}",
                                        fontSize = 12.sp,
                                        color = Color(0xFFFEF01B),
                                    )
                                }
                            }
                            Text(
                                text = curTime,
                                fontSize = 12.sp,
                                color = Color(0xFF556677),
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    // 내용
                    SelectionContainer {
                        Text(
                            text = "${message.content}",
                            modifier = Modifier
                                .background(
                                    color = if (isOwnMessage) Color(0xFFFEF01B) else Color.White,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(8.dp)
                                .widthIn(max = 220.dp),
                            color = Color.Black
                        )
                    }

                    // 다른 사람의 메시지인 경우, 메시지 뒤에 시간을 표시
                    if (!isOwnMessage &&
                        (nextMessage == null || nextTime != curTime || nextMessage.senderId != message.senderId)
                    ) {

                        Spacer(modifier = Modifier.width(4.dp))
                        Column(modifier = Modifier.align(Alignment.Bottom)) {
                            if (message.readCount!! > 0) {
                                Text(
                                    text = "${message.readCount}",
                                    fontSize = 12.sp,
                                    color = Color(0xFFFEF01B),
                                )
                            }
                            Text(
                                text = curTime,
                                fontSize = 12.sp,
                                color = Color(0xFF556677),
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputText(
    modifier: Modifier= Modifier,
    socketViewModel: SocketViewModel,
    listState: LazyListState,
    messages: List<MessageEntity>,
    chatId: String?,
    memberId: Long?,
    nickname: String,
    coroutineScope: CoroutineScope,
) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        IconButton(
            onClick = {
                val message = KafkaMessage(
                    message = text.text,
                    senderId = memberId,
                    sendTime = LocalDateTime.now(),
                    senderName = nickname
                )
                text = TextFieldValue("")
                coroutineScope.launch {
                    socketViewModel.sendMessage(message, chatId?.toLong())
                    delay(500)
                    listState.scrollToItem(Int.MAX_VALUE)
                    delay(500)
                    listState.scrollToItem(Int.MAX_VALUE)
                }
            },
            enabled = text.text.isNotBlank(),
            modifier = Modifier.padding(0.dp),
            content = {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = null,
                    tint = if (text.text.isNotBlank()) Color.Black else Color.Gray
                )
            }

        )
    }
}

fun formatTimestamp(timestamp: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val timestampWithoutMilliseconds = timestamp.substringBefore('.')
    val dateTime = LocalDateTime.parse(timestampWithoutMilliseconds, formatter)
    return "${dateTime.hour.toString().padStart(2, '0')}:${
        dateTime.minute.toString().padStart(2, '0')
    }"
}

fun formatDate(timestamp: String): String {
    val timestampWithoutMilliseconds = timestamp.substringBefore('T')
    val parseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(timestampWithoutMilliseconds, parseFormatter)
    val displayFormatter = DateTimeFormatter.ofPattern("yyyy'년' MM'월' dd'일' EEEE", Locale.KOREAN)
    return date.format(displayFormatter)
}