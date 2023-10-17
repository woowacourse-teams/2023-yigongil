package com.created.domain.model

data class StudySummary(
    val id: Long,
    val processingStatus: Int,
    val averageTier: Int,
    val name: String,
    val createdAt: String,
    val minimumWeeks: Int,
    val meetingDaysCountPerWeek: Int,
    val numberOfCurrentMembers: Int,
    val numberOfMaximumMembers: Int,
)
