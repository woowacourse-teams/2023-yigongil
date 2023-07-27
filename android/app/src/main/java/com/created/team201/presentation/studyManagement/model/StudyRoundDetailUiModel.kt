package com.created.team201.presentation.studyManagement.model

import com.created.domain.model.Role
import com.created.team201.presentation.home.model.TodoUiModel

data class StudyRoundDetailUiModel(
    val id: Long,
    val masterId: Long,
    val role: Role,
    val necessaryTodo: TodoUiModel,
    val optionalTodos: List<TodoUiModel>,
    val studyMembers: List<StudyMemberUiModel>,
)
