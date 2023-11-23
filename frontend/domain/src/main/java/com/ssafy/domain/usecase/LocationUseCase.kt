package com.ssafy.domain.usecase

import com.ssafy.domain.model.booking.BookingCreateRequest
import com.ssafy.domain.model.loacation.AddressResponse
import com.ssafy.domain.model.loacation.KakaoSearchResponse
import com.ssafy.domain.repository.LocationRepository
import retrofit2.Response
import javax.inject.Inject

class LocationUseCase @Inject constructor(private val repository: LocationRepository) {
    suspend fun getAddress(lng:String,lat:String): Response<AddressResponse> {
        return repository.getAddress(lng,lat)
    }
    suspend fun getSearchList(query:String,page:Int,size:Int,x:String,y:String,radius:Int): Response<KakaoSearchResponse> {
        return repository.getSearchList(query,page,size,x,y,radius)
    }
}
