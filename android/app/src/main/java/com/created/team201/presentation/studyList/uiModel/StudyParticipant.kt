package com.created.team201.presentation.studyList.uiModel

data class StudyParticipant(
    val isMaster: Boolean,
    val profileImageUrl: String,
    val name: String,
    val successRate: Int,
    val tier: String,
)
