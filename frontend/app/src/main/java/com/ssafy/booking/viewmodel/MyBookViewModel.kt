package com.ssafy.booking.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.booking.model.MyBookState
import com.ssafy.domain.model.mybook.MyBookListResponse
import com.ssafy.domain.model.mybook.MyBookMemoRegisterRequest
import com.ssafy.domain.model.mybook.MyBookRegisterRequest
import com.ssafy.domain.model.mybook.Notes
import com.ssafy.domain.usecase.MyBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MyBookViewModel @Inject constructor(
    private val myBookUseCase: MyBookUseCase
) : ViewModel() {

    private val _myBookState = MutableLiveData<MyBookState>()
    val myBookState : LiveData<MyBookState> get() = _myBookState

    private val _myBookResponse = MutableLiveData<Response<List<MyBookListResponse>>>()
    val myBookResponse : LiveData<Response<List<MyBookListResponse>>> get() = _myBookResponse

    fun getMyBookResponse(memberPk : Long) =
        viewModelScope.launch {
            _myBookState.value = MyBookState.Loading
            try {
                val myBookRes = myBookUseCase.getBookList(memberPk)
                if (myBookRes.isSuccessful && myBookRes.body() != null) {
                    _myBookState.value = MyBookState.Success(myBookRes.body()!!)
                }
            } catch (e: Exception) {
                _myBookState.value = MyBookState.Error(e.message ?: "알 수 없는 에러가 발생했습니다.")
            }
        }
    // 내 서재 상세 조회
    private val _notesList = MutableLiveData<List<Notes>>()
    val notesList: LiveData<List<Notes>> get() = _notesList

    fun removeNoteAtIndex(index: Int) {
        val currentList = _notesList.value ?: return

        // 'toMutableList()'를 사용하여 새로운 MutableList 인스턴스를 생성합니다.
        val updatedList = currentList.toMutableList().apply {
            // 인덱스를 확인하고 범위 내에 있는지 확인한 후 요소를 제거합니다.
            if (index in 0 until size) {
                removeAt(index)
            }
        }
        // MutableLiveData를 업데이트합니다.
        _notesList.value = updatedList
    }

    private val _myBookDetailResponse = MutableLiveData<Response<MyBookListResponse>>()
    val myBookDetailResponse : LiveData<Response<MyBookListResponse>> get() = _myBookDetailResponse
    fun getMyBookDetailResponse(memberPk: Long, isbn: String) =
        viewModelScope.launch {
            val response = myBookUseCase.getBookDetail(memberPk, isbn)
            _myBookDetailResponse.value = response
            response.body()?.let {response ->
                 _notesList.value = response.notes ?: emptyList()
            } ?: run {
                Log.d("메모가져오기", "메모 가져오던 중 에러 발생")
            }
        }

    private val _postBookRegisterResult = MutableLiveData<Response<Unit>>()
    val postBookRegisterResult: LiveData<Response<Unit>> get() = _postBookRegisterResult

    fun postBookRegister(request: MyBookRegisterRequest) =
        viewModelScope.launch {
            _postBookRegisterResult.value = myBookUseCase.postBookRegister(request)
        }

    // 메모 요청 날리기
    private val _postBookMemoResult = MutableLiveData<Response<Unit>>()
    val postBookMemoResult : LiveData<Response<Unit>> get() = _postBookMemoResult

    fun postBookMemo(request: MyBookMemoRegisterRequest, createdAt: String) =
        viewModelScope.launch {
            val currentList = _notesList.value.orEmpty()
            val updatedList = currentList + Notes(request.content, createdAt)
            _notesList.value = updatedList

            _postBookMemoResult.value = myBookUseCase.postBookMemo(request)
        }

    private val _deleteBookRegisterResult = MutableLiveData<Response<Unit>>()
    val deleteBookRegisterResult : LiveData<Response<Unit>> get() = _deleteBookRegisterResult

    fun deleteBookRegister(memberBookId: String) =
        viewModelScope.launch {
            _deleteBookRegisterResult.value = myBookUseCase.deleteBookRegister(memberBookId)
        }

    private val _delteBookNoteResult = MutableLiveData<Response<Unit>>()
    val delteBookNoteResult : LiveData<Response<Unit>> get() = _delteBookNoteResult

    fun deleteBookNote(memberBookId: String, noteIndex: Int) =
        viewModelScope.launch {
            _delteBookNoteResult.value = myBookUseCase.deleteBookNote(memberBookId, noteIndex)
        }
 }