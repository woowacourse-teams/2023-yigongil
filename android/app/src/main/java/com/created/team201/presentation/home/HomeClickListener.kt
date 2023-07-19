package com.created.team201.presentation.home

interface HomeClickListener {

    fun clickOnTodo(id: Int, isDone: Boolean)

    fun clickOnStudyCard()
}
