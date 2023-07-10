package com.created.team201.presentation.home.model

data class HomeUiModel(
    val studyName: String,
    val progressRate: Int,
    val leftDays: Int,
    val nextDate: String,
    val necessaryTodo: String,
    val optionalTodos: List<String>,
)
