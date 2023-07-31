package com.created.domain.model

data class Profile(
    val githubId: String,
    val id: Int,
    val introduction: String?,
    val nickname: String,
    val profileImageUrl: String?,
    val successRate: Double,
    val successfulRoundCount: Int,
    val tier: Int,
    val tierProgress: Int,
)
