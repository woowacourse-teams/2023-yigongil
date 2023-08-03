package com.created.team201.presentation.studyManagement.model

import com.created.domain.model.Role

data class StudyManagementInformationUiModel(
    val name: String,
    val numberOfCurrentMembers: Int,
    val numberOfMaximumMembers: Int,
    val role: Role,
    val startAt: String,
    val totalRoundCount: Int,
    val periodOfRound: String,
    val currentRound: Int,
    val introduction: String,
)
