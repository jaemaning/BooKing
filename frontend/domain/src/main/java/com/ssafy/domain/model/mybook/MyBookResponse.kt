package com.ssafy.domain.model.mybook

import com.google.gson.annotations.SerializedName
import com.ssafy.domain.model.booksearch.BookSearchResponse


data class Notes (
    val memo : String,
    val createdAt : String
)

data class MyBookListResponse (

    @SerializedName("id")
    val memberBookId : String,
    @SerializedName("memberPk")
    val memberPk : Long,
    @SerializedName("bookInfo")
    val bookInfo : BookSearchResponse,
    @SerializedName("notes")
    val notes : List<Notes>?,
    @SerializedName("createdAt")
    val createdAt: String
)