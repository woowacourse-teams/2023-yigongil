package com.created.team201.presentation.common

import com.created.team201.presentation.home.model.TodoUiModel

interface TodoClickListener {

    fun clickOnNecessaryTodoCheck(todo: TodoUiModel, roundId: Int, isDone: Boolean)

    fun clickOnOptionalTodoCheck(todo: TodoUiModel, roundId: Int, isDone: Boolean)
}
