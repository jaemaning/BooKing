package com.ssafy.domain.model.kakaopay

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime


//data class Amount (
//
//    @SerializedName("total")
//    val total : Long,
//    @SerializedName("tax_free")
//    val taxFree : Long,
//    @SerializedName("vat")
//    val vat : Long,
//    @SerializedName("point")
//    val point : Long,
//    @SerializedName("discount")
//    val discount : Long,
//    @SerializedName("green_deposit")
//    val greenDeposit : Long
//)

//data class KakaoPayResponse (
//
//    @SerializedName("partner_user_id")
//    val partnerUserId : String,
//    @SerializedName("partner_order_id")
//    val partnerOrderId : String,
//    @SerializedName("payment_method_type")
//    val paymentMethodType : String,
//    @SerializedName("item_name")
//    val itemName : String,
//    @SerializedName("quantity")
//    val quantity : Int,
//    @SerializedName("amount")
//    val amount : Amount,
//    @SerializedName("created_at")
//    val createdAt : String,
//    @SerializedName("approved_at")
//    val approvedAt : String,
//)

data class KakaoPayResponse (

    @SerializedName("tid")
    val tid: String,
    @SerializedName("next_redirect_app_url")
    val nextRedirectAppUrl: String,
    @SerializedName("next_redirect_mobile_url")
    val nextRedirectMobileUrl: String,
    @SerializedName("next_redirect_pc_url")
    val nextRedirectPcUrl: String,
    @SerializedName("android_app_scheme")
    val androidAppScheme: String,
    @SerializedName("ios_app_scheme")
    val iosAppScheme: String,
    @SerializedName("created_at")
    val createdAt: String
)