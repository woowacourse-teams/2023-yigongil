package com.created.team201.presentation.studyManagement.model

data class OptionalTodoUiModel(
    val todo: TodoUiModel,
    val viewType: Int,
) {

    companion object {
        val ADD_TODO = OptionalTodoUiModel(
            todo = TodoUiModel(0L, "", false),
            viewType = 1,
        )
    }
}
