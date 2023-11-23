package com.ssafy.domain.model.booksearch

import com.google.gson.annotations.SerializedName

data class BookSearchResponse (
    @SerializedName("title")
    val title: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("coverImage")
    val coverImage: String,
    @SerializedName("genre")
    val genre: String,
    @SerializedName("publishDate")
    val publishDate: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("isbn")
    val isbn : String
)

data class BookSearchPopularResponse (
    @SerializedName("title")
    val title: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("coverImage")
    val coverImage: String,
    @SerializedName("genre")
    val genre: String,
    @SerializedName("publishDate")
    val publishDate: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("isbn")
    val isbn : String,
    @SerializedName("meetingCnt")
    val meetingCnt : Int
)
