package com.created.team201.presentation.home.model

import com.created.team201.presentation.home.model.HomeUiModel.Grass.BOTTOM
import com.created.team201.presentation.home.model.HomeUiModel.Grass.MIDDLE
import com.created.team201.presentation.home.model.HomeUiModel.Grass.TOP

data class HomeUiModel(
    val studyName: String,
    val progressRate: Int,
    val leftDays: Int,
    val nextDate: String,
    val necessaryTodo: TodoUiModel,
    val optionalTodos: List<TodoUiModel>,
) {
    private val _grass: MutableList<Grass> = mutableListOf(
        TOP, TOP, TOP,
        MIDDLE, MIDDLE, MIDDLE,
        BOTTOM, BOTTOM, BOTTOM,
    )
    val grass: List<Grass> = _grass.toList()

    enum class Grass {
        TOP,
        MIDDLE,
        BOTTOM,
    }

    companion object {
        private const val GRASS_ALL_COUNT = 9
    }
}
