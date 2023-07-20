package com.created.domain.model

import com.created.domain.Period

data class StudyManage(
    val id: Long,
    val processingStatus: Int,
    val tier: Int,
    val role: Role,
    val title: String,
    val date: String,
    val totalRound: Int,
    val period: Period,
    val currentMember: Int,
    val maximumMember: Int,
)
