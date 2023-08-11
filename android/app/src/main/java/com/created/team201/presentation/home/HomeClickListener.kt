package com.created.team201.presentation.home

import com.created.team201.presentation.common.TodoClickListener
import com.created.team201.presentation.home.model.TodoUiModel

interface HomeClickListener : TodoClickListener {

    override fun clickOnNecessaryTodoCheck(todo: TodoUiModel, roundId: Int, isDone: Boolean)

    override fun clickOnOptionalTodoCheck(todo: TodoUiModel, roundId: Int, isDone: Boolean)

    fun clickOnStudyCard(studyId: Long)
}
