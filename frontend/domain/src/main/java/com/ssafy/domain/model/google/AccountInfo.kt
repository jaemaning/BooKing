package com.ssafy.domain.model.google

data class AccountInfo(val loginId: String ,val tokenId: String, val name: String, val type : Type) {
    enum class Type {
        GOOGLE
    }
}
