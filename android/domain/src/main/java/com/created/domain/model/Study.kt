package com.created.domain.model

data class Study(
    val studyId: Int,
    val studyName: String,
    val progressRate: Int,
    val leftDays: Int,
    val nextDate: String,
    val necessaryTodo: Todo,
    val optionalTodo: List<Todo>,
)
