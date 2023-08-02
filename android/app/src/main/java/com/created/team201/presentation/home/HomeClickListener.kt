package com.created.team201.presentation.home

import com.created.team201.presentation.common.TodoClickListener

interface HomeClickListener : TodoClickListener {

    override fun clickOnTodo(id: Long, isDone: Boolean)

    fun clickOnStudyCard(studyId: Long)
}
