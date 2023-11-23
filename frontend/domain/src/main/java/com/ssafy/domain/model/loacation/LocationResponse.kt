package com.ssafy.domain.model.loacation
import com.google.gson.annotations.SerializedName

// 위도,경도로 행정구역 받아오기.
data class AddressResponse(
    @SerializedName("meta") val meta: Meta,
    @SerializedName("documents") val documents: List<Document>
)

data class Meta(
    @SerializedName("total_count") val totalCount: Int
)

data class Document(
    @SerializedName("address") val address: Address,
    @SerializedName("road_address") val roadAddress: RoadAddress
)

data class Address(
    @SerializedName("address_name") val addressName: String,
    @SerializedName("region_1depth_name") val region1DepthName: String,
    @SerializedName("region_2depth_name") val region2DepthName: String,
    @SerializedName("region_3depth_name") val region3DepthName: String,
    @SerializedName("mountain_yn") val mountainYn: String,
    @SerializedName("main_address_no") val mainAddressNo: String,
    @SerializedName("sub_address_no") val subAddressNo: String
)

data class RoadAddress(
    @SerializedName("address_name") val addressName: String,
    @SerializedName("region_1depth_name") val region1DepthName: String,
    @SerializedName("region_2depth_name") val region2DepthName: String,
    @SerializedName("region_3depth_name") val region3DepthName: String,
    @SerializedName("road_name") val roadName: String,
    @SerializedName("underground_yn") val undergroundYn: String,
    @SerializedName("main_building_no") val mainBuildingNo: String,
    @SerializedName("sub_building_no") val subBuildingNo: String,
    @SerializedName("building_name") val buildingName: String,
    @SerializedName("zone_no") val zoneNo: String
)

// Kakao Search

// 카카오 장소 검색  API
data class KakaoSearchResponse(
    @SerializedName("meta") val searchMeta: SearchMeta,
    @SerializedName("documents") val documents: List<SearchDocument>
)

data class SearchMeta(
    @SerializedName("same_name") val sameName: SameName,
    @SerializedName("pageable_count") val pageableCount: Int,
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("is_end") val isEnd: Boolean
)

data class SameName(
    @SerializedName(" region") val region: List<String>,
    @SerializedName("keyword") val keyword: String,
    @SerializedName("selected_region") val selectedRegion: String
)

data class SearchDocument(
    @SerializedName("place_name") val placeName: String,
    @SerializedName("distance") val distance: String,
    @SerializedName("place_url") val placeUrl: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("address_name") val addressName: String,
    @SerializedName("road_address_name") val roadAddressName: String,
    @SerializedName("id") val id: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("category_group_code") val categoryGroupCode: String,
    @SerializedName("category_group_name") val categoryGroupName: String,
    @SerializedName("x") val x: String,
    @SerializedName("y") val y: String
)