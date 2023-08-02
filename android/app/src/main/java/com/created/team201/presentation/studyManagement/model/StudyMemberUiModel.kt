package com.created.team201.presentation.studyManagement.model

data class StudyMemberUiModel(
    val id: Long,
    val isMaster: Boolean,
    val nickname: String,
    val profileImageUrl: String,
    val isDone: Boolean,
) {
    fun progressPercentage(): Int = if (isDone) 100 else 0
}
