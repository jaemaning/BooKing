package com.ssafy.data.remote.api
import com.ssafy.domain.model.loacation.AddressResponse
import com.ssafy.domain.model.loacation.KakaoSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import com.ssafy.data.BuildConfig

interface LocationApi {
    // 위도,경도를 주소로 변환

    //    @Headers("Content-Type: application/json;charset=UTF-8",
    @Headers("Content-Type: application/json;charset=UTF-8","Authorization:KakaoAK ${BuildConfig.kakaoAK}")
    @GET("https://dapi.kakao.com/v2/local/geo/coord2address")
    suspend fun getAddress(@Query("y") lat:String,@Query("x") lng: String): Response<AddressResponse>

    // 키워드 검색
    @Headers("Content-Type: application/json;charset=UTF-8","Authorization:KakaoAK ${BuildConfig.kakaoAK}")
    @GET("https://dapi.kakao.com/v2/local/search/keyword")
    suspend fun getSearchList(@Query("query") query:String,@Query("page") page:Int,@Query("size") size:Int,@Query("y") y:String,@Query("x") x:String,@Query("radius") radius:Int): Response<KakaoSearchResponse>

}