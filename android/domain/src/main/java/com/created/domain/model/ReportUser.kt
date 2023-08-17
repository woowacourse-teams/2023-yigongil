package com.created.domain.model

data class ReportUser(
    val reportedMemberId: Long,
    val title: String,
    val problemOccuredAt: String,
    val content: String,
)
