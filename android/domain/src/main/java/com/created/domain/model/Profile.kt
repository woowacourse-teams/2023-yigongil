package com.created.domain.model

data class Profile(
    val githubId: String,
    val id: Long,
    val profileInformation: ProfileInformation,
    val profileImageUrl: String?,
    val successRate: Double,
    val successfulRoundCount: Int,
    val tier: Int,
    val tierProgress: Int,
) {
    fun updateProfileInformation(profileInformation: ProfileInformation): Profile = copy(
        profileInformation = profileInformation.updateMyProfile(profileInformation.nickname, profileInformation.introduction)
    )
}
