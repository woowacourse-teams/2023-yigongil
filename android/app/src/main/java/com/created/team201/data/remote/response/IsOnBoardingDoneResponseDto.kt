package com.created.team201.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class IsOnBoardingDoneResponseDto(
    val isOnboardingDone: Boolean,
)
