package com.ssafy.domain.repository

import com.ssafy.domain.model.mybook.MyBookListResponse
import com.ssafy.domain.model.mybook.MyBookMemoRegisterRequest
import com.ssafy.domain.model.mybook.MyBookRegisterRequest
import retrofit2.Response


interface MyBookRepository {

    suspend fun postBookRegister(request: MyBookRegisterRequest) : Response<Unit>

    suspend fun getBookList(memberPk : Long) : Response<List<MyBookListResponse>>

    suspend fun getBookDetail(memberPk: Long, isbn: String) : Response<MyBookListResponse>

    suspend fun postBookMemo(request: MyBookMemoRegisterRequest) : Response<Unit>

    suspend fun deleteBookRegister(memberBookId: String) : Response<Unit>

    suspend fun deleteBookNote(memberBookId: String, noteIndex: Int) : Response<Unit>
}