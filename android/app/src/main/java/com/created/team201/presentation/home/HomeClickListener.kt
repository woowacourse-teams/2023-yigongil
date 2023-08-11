package com.created.team201.presentation.home

import com.created.team201.presentation.common.TodoClickListener
import com.created.team201.presentation.home.model.TodoWithRoundIdUiModel

interface HomeClickListener : TodoClickListener {

    override fun clickOnNecessaryTodoCheck(todo: TodoWithRoundIdUiModel, isDone: Boolean)

    override fun clickOnOptionalTodoCheck(todo: TodoWithRoundIdUiModel, isDone: Boolean)

    fun clickOnStudyCard(studyId: Long)
}
