package com.ssafy.booking.model

import com.ssafy.booking.ui.profile.ProfileData

sealed class UserProfileState {
    object Loading : UserProfileState()
    data class Success(val data: ProfileData) : UserProfileState()
    data class Error(val message: String) : UserProfileState()
}
