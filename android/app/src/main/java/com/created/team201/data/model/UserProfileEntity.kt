package com.created.team201.data.model

import com.created.domain.model.Profile

data class UserProfileEntity(
    val profile: Profile,
    val finishedStudies: List<FinishedStudyEntity>,
)
