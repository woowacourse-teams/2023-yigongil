package com.created.team201.presentation.profile.model

data class FinishedStudyUiModel(
    val id: Long,
    val averageTier: Int,
    val isSucceed: Boolean,
    val name: String,
    val numberOfCurrentMembers: Int,
    val numberOfMaximumMembers: Int,
    val periodOfRound: String,
    val startAt: String,
    val totalRoundCount: Int,
)
