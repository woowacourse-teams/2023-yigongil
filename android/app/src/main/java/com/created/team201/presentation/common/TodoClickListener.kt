package com.created.team201.presentation.common

import com.created.team201.presentation.home.model.TodoWithRoundIdUiModel

interface TodoClickListener {

    fun clickOnNecessaryTodoCheck(todo: TodoWithRoundIdUiModel, isDone: Boolean)

    fun clickOnOptionalTodoCheck(todo: TodoWithRoundIdUiModel, isDone: Boolean)
}
