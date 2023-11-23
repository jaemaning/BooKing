package com.ssafy.domain.model.mybook


data class MyBookRegisterRequest (
    val memberPk: Long,
    val bookIsbn : String,
//    val memo : String,
)

data class MyBookMemoRegisterRequest (
    val memberPk: Long,
    val isbn: String,
    val content: String
)
