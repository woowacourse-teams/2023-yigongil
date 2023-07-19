package com.created.domain.model

data class Study(
    val studyId: Long,
    val studyName: String,
    val progressRate: Int,
    val leftDays: Int,
    val nextDate: String,
    val necessaryTodo: Todo,
    val optionalTodo: List<Todo>,
)
