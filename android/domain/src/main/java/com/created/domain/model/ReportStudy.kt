package com.created.domain.model

data class ReportStudy(
    val reportedStudyId: Long,
    val title: String,
    val problemOccurredAt: String,
    val content: String,
)
