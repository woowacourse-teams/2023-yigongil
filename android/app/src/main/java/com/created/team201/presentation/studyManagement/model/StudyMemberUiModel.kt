package com.created.team201.presentation.studyManagement.model

import com.created.team201.R

data class StudyMemberUiModel(
    val id: Long,
    val isMaster: Boolean,
    val nickname: String,
    val profileImageUrl: String,
    val isDone: Boolean,
    val isDeleted: Boolean,
) {
    fun progressPercentage(): Int = if (isDone) 100 else 0
    fun getIsDoneDescription(): Int {
        if (isDone) {
            return R.string.item_study_management_todo_done
        }
        return R.string.item_study_management_todo_undone
    }
}
