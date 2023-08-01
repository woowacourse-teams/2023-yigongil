package com.created.team201.presentation.profile.model

import com.created.team201.presentation.studyList.model.PeriodUiModel

data class FinishedStudyUiModel(
    val id: Long,
    val averageTier: Int,
    val isSucceed: Boolean,
    val name: String,
    val numberOfCurrentMembers: Int,
    val numberOfMaximumMembers: Int,
    val periodOfRound: PeriodUiModel,
    val startAt: String,
    val totalRoundCount: Int,
)
