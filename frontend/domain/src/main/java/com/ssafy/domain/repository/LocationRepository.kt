package com.ssafy.domain.repository

import com.ssafy.domain.model.booking.SearchResponse
import com.ssafy.domain.model.loacation.AddressResponse
import com.ssafy.domain.model.loacation.KakaoSearchResponse
import retrofit2.Response

interface LocationRepository {
    suspend fun getAddress(lng:String,lat:String) : Response<AddressResponse>
    suspend fun getSearchList(query:String,page:Int,size:Int,x:String,y:String,radius:Int) : Response<KakaoSearchResponse>
}