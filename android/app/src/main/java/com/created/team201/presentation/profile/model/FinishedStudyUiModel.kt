package com.created.team201.presentation.profile.model

data class FinishedStudyUiModel(
    val id: Long,
    val name: String,
    val averageTier: Int,
    val numberOfCurrentMembers: Int,
    val numberOfMaximumMembers: Int,
    val isSucceed: Boolean,
)
