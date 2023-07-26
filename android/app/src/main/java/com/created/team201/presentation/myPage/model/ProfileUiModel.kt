package com.created.team201.presentation.myPage.model

data class ProfileUiModel(
    val githubId: String,
    val id: Int,
    val introduction: String,
    val nickname: String,
    val profileImageUrl: String,
    val successRate: Double,
    val successfulRoundCount: Int,
    val tier: Int,
    val tierProgress: Int,
)
