package com.created.team201.presentation.studyManagement

import com.created.team201.presentation.common.TodoClickListener

interface StudyManagementClickListener : TodoClickListener {

    override fun clickOnTodo(id: Long, isDone: Boolean)

    fun onClickAddTodo(todoContent: String)

    fun onClickAddNecessaryTodo(todoContent: String)

    fun onClickAddOptionalTodo(optionalTodoCount: Int)

    fun onClickUpdateNecessaryTodoIsDone(isDone: Boolean)

    fun clickOnUpdateTodo(isNecessary: Boolean, todoContent: String)
}
