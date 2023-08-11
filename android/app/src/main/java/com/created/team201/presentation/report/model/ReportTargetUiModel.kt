package com.created.team201.presentation.report.model

data class ReportTargetUiModel(
    val reportedMemberId: Long,
    val title: String,
    val problemOccuredAt: String,
    val content: String,
)
