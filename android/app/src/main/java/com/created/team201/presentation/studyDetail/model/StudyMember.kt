package com.created.team201.presentation.studyDetail.model

data class StudyMember(
    val isMaster: Boolean,
    val profileImageUrl: String,
    val name: String,
    val successRate: Int,
    val tier: Int,
)
