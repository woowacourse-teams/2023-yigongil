package com.created.team201.presentation.myPage.model

data class ProfileUiModel(
    val githubId: String,
    val id: Long,
    val profileInformation: ProfileInformationUiModel,
    val profileImageUrl: String?,
    val successRate: Double,
    val successfulRoundCount: Int,
    val tier: Int,
    val tierProgress: Int,
)
