package com.created.team201.presentation.profile.model

import com.created.team201.presentation.myPage.model.ProfileUiModel

data class UserProfileUiModel(
    val profile: ProfileUiModel,
    val finishedStudies: List<FinishedStudyUiModel>,
)
