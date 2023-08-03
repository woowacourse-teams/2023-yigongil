package com.created.domain.model

data class UserProfile(
    val profile: Profile,
    val finishedStudies: List<FinishedStudy>
)
