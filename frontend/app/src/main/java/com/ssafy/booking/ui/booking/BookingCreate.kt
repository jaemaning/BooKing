import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
// import androidx.compose.foundation.gestures.ModifierLocalScrollableContainerProvider.value
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.MaterialTheme.colors
import coil.compose.AsyncImage
import com.google.common.io.Files.append
import com.ssafy.booking.R
import com.ssafy.booking.di.App
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.booking.bookingSetting.DatePickerComposable
import com.ssafy.booking.ui.booking.bookingSetting.FeeInputField
import com.ssafy.booking.ui.booking.bookingSetting.SetDateAndFeeBottomButton
import com.ssafy.booking.ui.booking.bookingSetting.SetEntryFee
import com.ssafy.booking.ui.booking.bookingSetting.TimePickerComposable
import com.ssafy.booking.ui.common.BackTopBar
import com.ssafy.booking.ui.common.TopBar
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.BookSearchViewModel
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.booking.viewmodel.LocationViewModel
import com.ssafy.domain.model.booking.BookingCreateRequest
import com.ssafy.domain.model.booking.BookingStartRequest
import com.ssafy.domain.model.booksearch.BookSearchResponse
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingCreate(navController: NavController, appViewModel: AppViewModel, isbn: String?) {
    // 상태값들 최상단에 정의
    var meetingTitle by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var description by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var hashTagText by remember { mutableStateOf(listOf<String>()) }

//    var bookIsbn by remember { mutableStateOf(TextFieldValue(isbn)) }
    var maxParticipants by remember { mutableStateOf(2) }

    // 뷰모델
    val viewModel: BookingViewModel = hiltViewModel()
    val locationViewModel : LocationViewModel = hiltViewModel()
    val postCreateBookingResponse by viewModel.postCreateBookingResponse.observeAsState()
    val createBookingSuccess by viewModel.createBookingSuccess.observeAsState()
    // isbn 으로 데이터 불러오기
    val bookSearchViewModel: BookSearchViewModel = hiltViewModel()
    val getBookSearchByIsbnResponse by bookSearchViewModel.getBookSearchByIsbnResponse.observeAsState()
    val address = App.prefs.getShortUserAddress()



    LaunchedEffect(Unit){
        isbn?.let {
            bookSearchViewModel.getBookSearchByIsbn(isbn)
        }
    }

    LaunchedEffect(createBookingSuccess) {
        createBookingSuccess?.let { success ->
            if (success) {
                navController.navigate(AppNavItem.Main.route) {
                    popUpTo("login") { inclusive = true }
                    launchSingleTop = true
                }
                // 상태를 리셋하여 중복 네비게이션을 방지합니다.
                viewModel.resetCreateBookingSuccess()
            }
        }
    }
    Scaffold(
        topBar = {
                 BackTopBar(title = "모임 생성하기")
        },
        bottomBar = {
            CreateBookingButton(
                bookIsbn = "$isbn",
                meetingTitle = meetingTitle.text, // TextFieldValue에서 String으로 변환
                description = description.text,
                maxParticipants = maxParticipants,
                hashtagList = hashTagText,
                address = address?:"",
            )
        }
    ) { padingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()) // 스크롤을 위한 수정자 추가
            ) {
                BookSearch(getBookSearchByIsbnResponse)
                TextFieldsSection(
                    meetingTitle = meetingTitle,
                    onMeetingTitleChanged = { meetingTitle = it },
                    description = description,
                    onDescriptionChanged = { description = it },
                    hashTagText = hashTagText,
                    onAddHashTag = { tag ->
                        if (!hashTagText.contains(tag)) {
                            hashTagText = hashTagText + tag
                        }
                    },
                    onRemoveHashTag = { tag ->
                        hashTagText = hashTagText.filter { it != tag }
                    }
                )
                ParticipantCounter(
                    maxParticipants = maxParticipants,
                    onMaxParticipantsChanged = { newCount -> maxParticipants = newCount }

                )

            }
        }
    }
}

@Composable
fun BookSearch(
    getBookSearchByIsbnResponse: Response<BookSearchResponse>?
) {
    val navController = LocalNavigation.current

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
                .background(Color.White)
                .width(150.dp)
                .height(210.dp)
                .clickable {
                    navController.navigate("book/1")
                }
                .border(2.dp, Color.LightGray, RoundedCornerShape(6.dp))
        ) {
            // 왼쪽의 도서 등록 칸
            getBookSearchByIsbnResponse?.let {
                val book = it.body()
                AsyncImage(
                    model = book!!.coverImage,
                    contentDescription = "북 커버",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .width(152.dp)
                        .height(212.dp)

                )
            } ?: run {@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SetDateAndFee() {
//val bookingViewModel: BookingViewModel = hiltViewModel()
//val dateState by bookingViewModel.date.observeAsState()
//val timeState by bookingViewModel.time.observeAsState()
//val feeState by bookingViewModel.fee.observeAsState()
//val placeNameState by bookingViewModel.placeName.observeAsState()
//val locationState by bookingViewModel.location.observeAsState()
//
//Scaffold(
//    bottomBar = {
//        if (dateState != null && timeState != null && feeState != null) {
//            // 바텀 버튼을 Scaffold의 bottomBar로 설정합니다.
//            SetDateAndFeeBottomButton(dateState!!, timeState!!, feeState, bookingViewModel)
//        }
//    }
//) { innerPadding ->
//    Column(modifier = Modifier.padding(innerPadding)) {
//        DatePickerComposable(onDateSelected = { date ->
//            bookingViewModel.date.value = date
//        })
//        TimePickerComposable(onTimeSelected = { time ->
//            bookingViewModel.time.value = time
//        })
//        Text("선택된 날짜: ${dateState ?: "없음"}")
//        Text("선택된 시간: ${timeState ?: "없음"}")
//        SetEntryFee(bookingViewModel)
//    }
//}
//}
//
//@Composable
//fun DatePickerComposable(onDateSelected: (LocalDate) -> Unit) {
//    val context = LocalContext.current
//    val currentDate = LocalDate.now()
//    val datePickerDialog = remember {
//        DatePickerDialog(context, { _, year, month, dayOfMonth ->
//            onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
//        }, currentDate.year, currentDate.monthValue - 1, currentDate.dayOfMonth).apply {
//            // 오늘과 오늘 이후만 선택가능하게 예외처리
//            datePicker.minDate = System.currentTimeMillis() - 1000
//        }
//    }
//    Button(onClick = { datePickerDialog.show() }) {
//        Text("날짜 선택")
//    }
//}
//
//@Composable
//fun TimePickerComposable(onTimeSelected: (LocalTime) -> Unit) {
//    val context = LocalContext.current
//    val timePickerDialog = remember {
//        TimePickerDialog(context, { _, hour, minute ->
//            onTimeSelected(LocalTime.of(hour, minute))
//        }, LocalTime.now().hour, LocalTime.now().minute, true)
//    }
//    Button(onClick = { timePickerDialog.show() }) {
//        Text("시간 선택")
//    }
//}
//
//@Composable
//fun SetEntryFee(modifier: Modifier = Modifier) {
//    var enteredFee by remember { mutableStateOf(0) }
//
//    Column(modifier = modifier) {
//
//        FeeInputField(bookingViewModel = BookingViewModel)
//        Text("설정된 참가비: $enteredFee 원")
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun FeeInputField(onFeeChanged: (Int) -> Unit) {
//    var fee by remember { mutableStateOf(0) }
//    val bookingViewModel: BookingViewModel = hiltViewModel()
//    Column(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        OutlinedTextField(
//            value = fee.toString(),
//            onValueChange = { newValue ->
//                fee = newValue.toIntOrNull() ?: 0
//                onFeeChanged(fee)
//                bookingViewModel.fee.value = fee
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
//fun SetDateAndFeeBottomButton(
//    dateState: LocalDate,
//    timeState: LocalTime,
//    feeState: Int?,
//    bookingViewModel: BookingViewModel
//) {
//    // 현재 Composable 함수와 연관된 Context 가져오기
//    val context = LocalContext.current
//    val navController = LocalNavigation.current
//
//    val bookingStartRequestResponse by bookingViewModel.postBookingStartResponse.observeAsState()
//    LaunchedEffect(bookingStartRequestResponse) {
//        bookingStartRequestResponse?.let { response ->
//            if (response.isSuccessful) { // Assuming 'isSuccessful' is a flag in your response indicating success
//                val meetingId = App.prefs.getMeetingId()
//                navController.navigate("bookingDetail/$meetingId") {
//                    popUpTo("booking/setting/location") { inclusive = true }
//                    launchSingleTop = true
//                }
//            }
//        }
//    }
//
//                    Box(
//                        contentAlignment = Alignment.BottomCenter,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                    ) {
//                        Button(
//                            onClick = {
//                                val date =
//                                    LocalDateTime.of(bookingViewModel.date.value, bookingViewModel.time.value)
//                                        .withSecond(0)
//                                Log.d("date", date.toString())
//                                Log.d("date", feeState.toString())
//                                Log.d("date", dateState.toString())
//                                Log.d("date", timeState.toString())
//                                if (date == null || feeState == null) {
//                                    // 제목 또는 내용이 비어있을 경우 Toast 메시지 표시
//                                    Toast.makeText(context, "독서모임의 모임 일정과 참가비를 모두 입력해주세요.", Toast.LENGTH_LONG)
//                                        .show()
//                                } else {
//                                    Log.d("date123", date.toString())
//                                    val request = BookingStartRequest(
//                                        meetingId = App.prefs.getMeetingId()!!,
//                                        date = date.toString(),
//                                        fee = bookingViewModel.fee.value!!,
//                                        lat = App.prefs.getMeetingLat()!!.toDouble(),
//                                        lgt = App.prefs.getMeetingLgt()!!.toDouble(),
//                                        address = App.prefs.getMeetingAddress()!!,
//                                        location = App.prefs.getMeetingLocation()!!,
//                                    )
//                                    bookingViewModel.postBookingStart(request)
//                                }
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(48.dp),
//                            shape = RoundedCornerShape(3.dp)
//                        ) {
//                            androidx.wear.compose.material.Text("다음", style = MaterialTheme.typography.bodyMedium)
//                        }
//                    }
//                }
                Icon(
//                    imageVector = Icons.Default.AddCircle,
                    painter = painterResource(id = R.drawable.outline_auto_stories_24),
                    contentDescription = null,
//                    tint = Color(0xFF12BD7E),
                    tint = Color.LightGray,
                    modifier = Modifier.size(80.dp)
                )
            }
        }
        getBookSearchByIsbnResponse?.let {
            val book = it.body()
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "${book!!.title}",
                fontSize = 16.sp,
            )
            Text(
                text = "${book!!.author}",
                fontSize = 11.sp,
            )
        }

        Spacer(modifier = Modifier.height(45.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldsSection(
    meetingTitle: TextFieldValue,
    onMeetingTitleChanged: (TextFieldValue) -> Unit,
    description: TextFieldValue,
    onDescriptionChanged: (TextFieldValue) -> Unit,
    hashTagText: List<String>,
    onAddHashTag: (String) -> Unit,
    onRemoveHashTag: (String) -> Unit
) {
    Text(text = "모임 제목",fontSize=20.sp)
    Spacer(modifier = Modifier
        .height(12.dp)
        .fillMaxWidth()
        .padding(end = 10.dp))
    OutlinedTextField(
        value = meetingTitle,
        onValueChange = onMeetingTitleChanged,
        placeholder = { Text("모임 제목") },
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color(0xFF12BD7E)),

        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .padding(end = 10.dp)
    )
    Spacer(modifier = Modifier.height(24.dp))

    Text(text = "모임 소개",fontSize=20.sp)
    Spacer(modifier = Modifier
        .height(12.dp)
        .fillMaxWidth()
        .padding(end = 10.dp))
    OutlinedTextField(
        value = description,
        colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color(0xFF12BD7E)),
        onValueChange = onDescriptionChanged,
        placeholder = { Text(text = buildAnnotatedString {
            append("독서모임에 대해 간단하게 설명해주세요.\n")
            append("(욕설, 비방하는 글, 상업적인 게시글은 \n통보없이 삭제될 수 있습니다.)\n")
            append("\n건강한 북킹 문화 조성을 함께 해요.")
        } )},
        maxLines = 6, // 최대 6줄 입력 가능
        modifier = Modifier
            .height(192.dp)
            .fillMaxWidth()
            .padding(end = 10.dp)
    )
    Spacer(modifier = Modifier
        .height(24.dp)
        .width(300.dp))
    Text(text = "해시 태그",fontSize=20.sp)
    Spacer(modifier = Modifier.height(12.dp))
    HashTagEditor(
        hashTagText = hashTagText,
        onAddHashTag = onAddHashTag,
        onRemoveHashTag = onRemoveHashTag
    )

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun CreateBookingButton(
    bookIsbn: String,
    meetingTitle: String,
    description: String,
    maxParticipants: Int,
    hashtagList: List<String>,
    address: String,
    viewModel: BookingViewModel = hiltViewModel()
) {
    Button(
        onClick = {
            val request = BookingCreateRequest(
                bookIsbn = bookIsbn,
                meetingTitle = meetingTitle,
                description = description,
                maxParticipants = maxParticipants,
                hashtagList = hashtagList,
                address = address
            )
            viewModel.postCreateBooking(request)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp) // 좌우 패딩 24dp, 아래 패딩 8dp
            .height(60.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C68E)),
        shape = RoundedCornerShape(4.dp) // 모서리 둥글게 4dp
    ) {
        Text(
            text = "모임 생성하기",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.gowundodum))
        )
    }
}
@Composable
fun ParticipantCounter(
    maxParticipants: Int,
    onMaxParticipantsChanged: (Int) -> Unit
) {
    // 참가자 수를 추적하는 상태 변수
    // 수평으로 정렬된 구성요소들을 포함하는 Row
    Text(text = "모임 인원 ( 2-6명 )",fontSize=20.sp)
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        // 참가자 수를 줄이는 버튼
        Button(
            onClick = { if (maxParticipants > 1)onMaxParticipantsChanged(maxParticipants - 1) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AD97)),
            shape = CircleShape,
            enabled = maxParticipants > 2, // 1보다 작아질 수 없도록 비활성화,
//            colors = ButtonDefaults.buttonColors(Color(0xFf00C68E))
        ) {
            Text("-",fontSize = 25.sp)
        }
        // 현재 참가자 수를 보여주는 Text
        Text(
            text = " $maxParticipants 명",
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
        )
        // 참가자 수를 늘리는 버튼
        Button(
            onClick = { if (maxParticipants < 6) onMaxParticipantsChanged(maxParticipants + 1) },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AD97)),
            enabled = maxParticipants < 6
//            colors = ButtonDefaults.buttonColors(Color(0xFf00C68E))
        ) {
            Text("+",fontSize = 25.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HashTagEditor(
    hashTagText: List<String>,
    onAddHashTag: (String) -> Unit,
    onRemoveHashTag: (String) -> Unit
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                if (newText.text.length <= 5) {
                    text = newText
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color(0xFF12BD7E)),
            singleLine = true,
            placeholder = { Text("해시태그") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (text.text.isNotBlank() && text.text.length <= 4) {
                        // '완료(Done)' 버튼이 눌렸을 때 할 작업
                        onAddHashTag(text.text.trim())
                        text = TextFieldValue("")
                        keyboardController?.hide()
                    }
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp)
                .onKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Enter && text.text.isNotBlank() && text.text.length <= 5) {
                        onAddHashTag(text.text.trim())
                        text = TextFieldValue("") // Reset text field
                        keyboardController?.hide()
                        true // Event consumed
                    } else {
                        false
                    }
                }
        )
        Row(
            Modifier
                .padding(top = 10.dp)
        ) {
            hashTagText.forEach { tag ->
                Button(
                    modifier = Modifier.padding(end = 10.dp),
                    shape = RoundedCornerShape(4.dp),
                    onClick = { onRemoveHashTag(tag) }
                ) {
                    Text(text = "#$tag")
                }
            }
        }
    }
}
