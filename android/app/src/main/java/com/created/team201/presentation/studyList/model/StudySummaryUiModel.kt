package com.created.team201.presentation.studyList.model

data class StudySummaryUiModel(
    val id: Long,
    val processingStatus: StudyStatus,
    val averageTier: Int,
    val name: String,
    val date: String,
    val totalRound: Int,
    val period: PeriodUiModel,
    val numberOfCurrentMembers: Int,
    val numberOfMaximumMembers: Int,
)
