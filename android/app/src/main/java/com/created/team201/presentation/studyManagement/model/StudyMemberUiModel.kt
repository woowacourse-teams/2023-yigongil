package com.created.team201.presentation.studyManagement.model

data class StudyMemberUiModel(
    val id: Long,
    val nickname: String,
    val profileImageUrl: String,
    val isDone: Boolean,
)
