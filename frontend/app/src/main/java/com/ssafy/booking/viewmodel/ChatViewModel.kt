package com.ssafy.booking.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.ssafy.data.room.dao.ChatDao
import com.ssafy.data.room.dao.MessageDao
import com.ssafy.data.room.entity.ChatEntity
import com.ssafy.data.room.entity.MessageEntity
import com.ssafy.domain.model.ChatCreateRequest
import com.ssafy.domain.model.ChatExitRequest
import com.ssafy.domain.model.ChatJoinRequest
import com.ssafy.domain.model.ChatRoom
import com.ssafy.domain.model.LastReadMessageRequest
import com.ssafy.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val chatDao: ChatDao,
    private val messageDao: MessageDao
) : ViewModel() {

    private val _chatListState = MutableLiveData<List<ChatRoom>>(listOf())
    val chatListState: LiveData<List<ChatRoom>> = _chatListState
    var errorMessage = mutableStateOf("")

    // 채팅방 생성
    fun createChatRoom(request: ChatCreateRequest) {
        viewModelScope.launch {
            try {
                val response = chatRepository.postChatCreate(request)
                if (response.isSuccessful) {
                    Log.d("CHAT", "CHATVM Chat room created successfully: $response")
                    loadChatList()
                } else {
                    Log.e("CHAT", "CHATVM Error creating chat room: $response")
                }
            } catch (e: Exception) {
                Log.e("CHAT", "CHATVM Exception creating chat room", e)
            }
        }
    }

    // 채팅방 참여
    fun joinChatRoom(request: ChatJoinRequest) {
        viewModelScope.launch {
            try {
                val response = chatRepository.postChatJoin(request)
                if (response.isSuccessful) {
                    Log.d("CHAT", "CHATVM Chat room joined successfully: $response")
                    loadChatList()
                } else {
                    Log.e("CHAT", "CHATVM Error joining chat room: $response")
                }
            } catch (e: Exception) {
                Log.e("CHAT", "CHATVM Exception joining chat room", e)
            }
        }
    }

    // 채팅방 나가기
    fun exitChatRoom(request: ChatExitRequest) {
        viewModelScope.launch {
            try {
                val response = chatRepository.postChatExit(request)
                if (response.isSuccessful) {
                    Log.d("CHAT", "CHATVM Chat room exited successfully: $response")
                    loadChatList()
                } else {
                    Log.e("CHAT", "CHATVM Error exiting chat room: $response")
                }
            } catch (e: Exception) {
                Log.e("CHAT", "CHATVM Exception exiting chat room", e)
            }
        }
    }

    // 채팅방 목록 불러오기
    fun loadChatList() {
        viewModelScope.launch {
            try {
                val chatList = chatRepository.getChatList()
                _chatListState.value = chatList
                Log.d("CHAT", "CHATVM Get Chat room List $chatListState")
                Log.d("CHAT", "CHATVM Get Chat room List ${chatListState.value}")
            } catch (e: HttpException) {
                errorMessage.value = "CHATVM 네트워크 에러: ${e.code()} ${e.message}"
                Log.d("CHAT", "$errorMessage.value")
            } catch (e: IOException) {
                errorMessage.value = "CHATVM 네트워크 연결을 확인해 주세요."
                Log.d("CHAT", "$errorMessage.value")
            } catch (e: Exception) {
                errorMessage.value = "CHATVM 알 수 없는 에러 발생: ${e.message}"
                Log.d("CHAT", "$errorMessage.value")
            }
        }
    }

    // 처음 들어가는 채팅방에 대한 정보 저장
    fun saveLocalChatId(chatroomId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newChatEntity = ChatEntity(chatroomId = chatroomId, lastMessageIdx = 1)
            chatDao.insertChatIdFirstTime(newChatEntity)
        }
    }

    // Room에서 마지막으로 읽은 메시지 GET
    private val _lastReadMessageIds = mutableStateOf(mutableMapOf<Int, Int>())
    val lastReadMessageIds: State<Map<Int, Int>> = _lastReadMessageIds
    fun getLastReadMessageId(chatroomId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val lastReadId = chatDao.getLastReadMessageId(chatroomId) ?: 0
            _lastReadMessageIds.value = _lastReadMessageIds.value.toMutableMap().apply {
                put(chatroomId, lastReadId)
            }
        }
    }

    // 2초마다 목록 조회 폴링
    private val _isChatHome = MutableLiveData<Boolean>(false)
    val isChatHome: LiveData<Boolean> get() = _isChatHome
    private var chatListJob: Job? = null
    fun startChatListAutoUpdate() {
        chatListJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                loadChatList()
                delay(2000L)
            }
        }
    }
    fun stopChatListAutoUpdate() {
        chatListJob?.cancel()
    }
    fun setIsChatHome(isHome: Boolean) {
        _isChatHome.value = isHome
        if (isHome) {
            startChatListAutoUpdate()
        } else {
            stopChatListAutoUpdate()
        }
    }

}
