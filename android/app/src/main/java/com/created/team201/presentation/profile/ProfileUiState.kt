package com.created.team201.presentation.profile

import com.created.team201.data.model.UserProfileEntity

sealed interface ProfileUiState {
    data class Success(val userProfile: UserProfileEntity) : ProfileUiState
    object Failure : ProfileUiState
    object Loading : ProfileUiState
}
