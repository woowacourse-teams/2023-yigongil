package com.created.team201.presentation.studyManagement

import com.created.team201.presentation.studyManagement.model.OptionalTodoUiModel

interface StudyManagementClickListener {

    fun onClickAddTodo(isNecessary: Boolean, todoContent: String)

    fun onClickUpdateTodoIsDone(isNecessary: Boolean, todoId: Long, isDone: Boolean)

    fun onClickGenerateOptionalTodo(): TodoState

    fun onClickEditNecessaryTodo(todoContents: String): TodoState

    fun onClickEditOptionalTodo(updatedTodos: List<OptionalTodoUiModel>): TodoState

    fun onClickDeleteOptionalTodo(todo: OptionalTodoUiModel)
}
