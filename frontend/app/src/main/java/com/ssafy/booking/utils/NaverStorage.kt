package com.ssafy.booking.utils


import com.ssafy.booking.di.App
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.URLEncoder
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


class ObjectStorageInterceptor(private val accessKey: String, private val secretKey: String, private val region: String) :
    Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
        val originalRequest = chain.request()
        val timestamp = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date())
        val date = timestamp.substring(0, 8)

        val standardizedQueryParameters = getStandardizedQueryParameters(originalRequest.url.query)
        val signedHeaders = getSignedHeaders(originalRequest.headers)
        val canonicalHeaders = getCanonicalHeaders(originalRequest.headers)
        val canonicalRequest = buildCanonicalRequest(originalRequest, standardizedQueryParameters, canonicalHeaders, signedHeaders)
        val scope = buildScope(date, region)

        val stringToSign = buildStringToSign(timestamp, scope, canonicalRequest)
        val signature = calculateSignature(stringToSign, date)

        val authorizationHeader = buildAuthorizationHeader(accessKey, scope, signedHeaders, signature)
        val payloadHash = "UNSIGNED-PAYLOAD"

        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", authorizationHeader)
            .addHeader("x-amz-date", timestamp)
            .addHeader("x-amz-content-sha256", payloadHash)
            .build()

        App.prefs.putNcpAuthHeader(authorizationHeader)
        App.prefs.putNcpTimeStamp(timestamp)
        App.prefs.putNcpPayloadHash(payloadHash)

        return chain.proceed(newRequest)
    }

    private fun getStandardizedQueryParameters(query: String?): String {
        if (query.isNullOrEmpty()) return ""

        val queryPairs = query.split("&").map {
            it.split("=").let { pair ->
                val key = URLEncoder.encode(pair[0], "UTF-8")
                val value = if (pair.size > 1) URLEncoder.encode(pair[1], "UTF-8") else ""
                key to value
            }
        }.toMap()

        return queryPairs.toSortedMap().entries.joinToString("&") {
            "${it.key}=${it.value}"
        }
    }

    private fun getSignedHeaders(headers: Headers): String {
        return headers.names().sorted().joinToString(";") { it.toLowerCase(Locale.US) }
    }

    private fun getCanonicalHeaders(headers: Headers): String {
        return headers.toMultimap().entries.sortedBy { it.key.toLowerCase(Locale.US) }
            .joinToString("") { entry ->
                "${entry.key.toLowerCase(Locale.US)}:${entry.value.joinToString(",").trim()}\n"
            }
    }

    private fun calculateSignature(stringToSign: String, date: String): String {
        val kSecret = "AWS4$secretKey".toByteArray(Charsets.UTF_8)
        val kDate = hmacSha256(date, kSecret)
        val kRegion = hmacSha256(region, kDate)
        val kService = hmacSha256("s3", kRegion)
        val kSigning = hmacSha256("aws4_request", kService)
        return hex(hmacSha256(stringToSign, kSigning))
    }

    private fun hmacSha256(data: String, key: ByteArray): ByteArray {
        val algorithm = "HmacSHA256"
        val mac = Mac.getInstance(algorithm)
        mac.init(SecretKeySpec(key, algorithm))
        return mac.doFinal(data.toByteArray(Charsets.UTF_8))
    }

    private fun hex(data: ByteArray): String {
        return data.joinToString("") { String.format("%02x", it) }
    }

    private fun buildStringToSign(timestamp: String, scope: String, canonicalRequest: String): String {
        val hashedCanonicalRequest = hash(canonicalRequest)
        return "AWS4-HMAC-SHA256\n$timestamp\n$scope\n$hashedCanonicalRequest"
    }

    private fun hash(text: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(text.toByteArray())
        return hex(digest)
    }

    private fun buildCanonicalRequest(request: Request, standardizedQueryParameters: String, canonicalHeaders: String, signedHeaders: String): String {
        val method = request.method
        val canonicalUri = request.url.toUri().path
        val hashedPayload = "UNSIGNED-PAYLOAD" // 혹은 payload의 실제 해시값
        return "$method\n$canonicalUri\n$standardizedQueryParameters\n$canonicalHeaders\n$signedHeaders\n$hashedPayload"
    }
    private fun buildScope(date: String, region: String): String {
        return "$date/$region/s3/aws4_request"
    }

    private fun buildAuthorizationHeader(accessKey: String, scope: String, signedHeaders: String, signature: String): String {
        return "AWS4-HMAC-SHA256 Credential=$accessKey/$scope, SignedHeaders=$signedHeaders, Signature=$signature"
    }

}
