package com.ssafy.domain.usecase

import com.ssafy.domain.model.mybook.MyBookListResponse
import com.ssafy.domain.model.mybook.MyBookMemoRegisterRequest
import com.ssafy.domain.model.mybook.MyBookRegisterRequest
import com.ssafy.domain.repository.MyBookRepository
import retrofit2.Response
import javax.inject.Inject

class MyBookUseCase @Inject constructor(
    private val repository: MyBookRepository
) {

    suspend fun postBookRegister(request: MyBookRegisterRequest) : Response<Unit> {
        return repository.postBookRegister(request)
    }

    suspend fun getBookList(memberPk: Long) : Response<List<MyBookListResponse>> {
        return repository.getBookList(memberPk)
    }

    suspend fun getBookDetail(memberPk: Long, isbn: String) : Response<MyBookListResponse> {
        return repository.getBookDetail(memberPk, isbn)
    }

    suspend fun postBookMemo(request: MyBookMemoRegisterRequest) : Response<Unit> {
        return repository.postBookMemo(request)
    }

    suspend fun deleteBookRegister(memberBookId: String) : Response<Unit> {
        return repository.deleteBookRegister(memberBookId)
    }

    suspend fun deleteBookNote(memberBookId: String, noteIndex: Int) : Response<Unit> {
        return repository.deleteBookNote(memberBookId, noteIndex)
    }
}