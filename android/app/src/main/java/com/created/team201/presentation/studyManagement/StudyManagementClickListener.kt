package com.created.team201.presentation.studyManagement

import com.created.team201.presentation.common.TodoClickListener
import com.created.team201.presentation.home.model.TodoUiModel

interface StudyManagementClickListener : TodoClickListener {

    override fun clickOnTodo(id: Long, isDone: Boolean)

    fun onClickAddTodo(todoContent: String)

    fun onClickAddOptionalTodo(optionalTodoCount: Int)

    fun clickOnUpdateTodo(todo: TodoUiModel, todoContent: String)
}
