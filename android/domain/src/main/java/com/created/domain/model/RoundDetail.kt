package com.created.domain.model

data class RoundDetail(
    val id: Long,
    val masterId: Long,
    val role: Role,
    val necessaryTodo: Todo,
    val optionalTodos: List<Todo>,
    val members: List<StudyMember>,
)
