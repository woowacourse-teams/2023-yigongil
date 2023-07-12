package com.created.team201.presentation.studyList

data class StudySummaryUiModel(
    val id: Long,
    val processingStatus: Int,
    val tier: Int,
    val title: String,
    val date: String,
    val period: String,
    val currentMember: Int,
    val maximumMember: Int
)
