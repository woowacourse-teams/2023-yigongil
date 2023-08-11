package com.created.team201.presentation.studyManagement.model

import com.created.domain.model.Role

data class StudyRoundDetailUiModel(
    val id: Long,
    val masterId: Long,
    val role: Role,
    val necessaryTodo: NecessaryTodoUiModel,
    val optionalTodos: List<OptionalTodoUiModel>,
    val studyMembers: List<StudyMemberUiModel>,
) {

    fun isMaster(): Boolean = role == Role.MASTER
}
