package com.created.domain.model

data class StudyManage(
    val id: Long,
    val processingStatus: Int,
    val tier: Int,
    val role: Role,
    val title: String,
    val date: String,
    val totalRound: Int,
    val period: String,
    val currentMember: Int,
    val maximumMember: Int,
)
