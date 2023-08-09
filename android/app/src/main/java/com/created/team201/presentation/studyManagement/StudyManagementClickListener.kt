package com.created.team201.presentation.studyManagement

interface StudyManagementClickListener{

    fun onClickAddTodo(isNecessary: Boolean, todoContent: String)

    fun onClickUpdateTodoIsDone(isNecessary: Boolean, todoId: Long, isDone: Boolean)

    fun onClickGenerateOptionalTodo(optionalTodoCount: Int)

    fun clickOnUpdateTodo(isNecessary: Boolean, todoContent: String)
}
