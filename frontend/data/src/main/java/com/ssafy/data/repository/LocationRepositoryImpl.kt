package com.ssafy.data.repository

import com.ssafy.data.remote.api.BookingApi
import com.ssafy.data.remote.api.LocationApi
import com.ssafy.domain.model.booking.BookingAll
import com.ssafy.domain.model.booking.BookingCreateRequest
import com.ssafy.domain.model.booking.BookingDetail
import com.ssafy.domain.model.loacation.AddressResponse
import com.ssafy.domain.model.loacation.KakaoSearchResponse
import com.ssafy.domain.repository.BookingRepository
import com.ssafy.domain.repository.LocationRepository
import retrofit2.Response
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationApi: LocationApi) : LocationRepository {
    override suspend fun getAddress(lng: String, lat: String): Response<AddressResponse> {
        return locationApi.getAddress(lng, lat)
    }

    override suspend fun getSearchList(query: String, page: Int, size: Int,x:String,y:String,radius:Int): Response<KakaoSearchResponse> {
        return locationApi.getSearchList(query, page, size, x,y,radius)
    }
}
