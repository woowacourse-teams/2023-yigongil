package com.created.team201.presentation.home.model

import com.created.team201.presentation.home.model.Grass.GrassType.BOTTOM
import com.created.team201.presentation.home.model.Grass.GrassType.MIDDLE
import com.created.team201.presentation.home.model.Grass.GrassType.TOP

data class HomeUiModel(
    val studyName: String,
    val progressRate: Int,
    val leftDays: Int,
    val nextDate: String,
    val necessaryTodo: TodoUiModel,
    val optionalTodos: List<TodoUiModel>,
) {
    private val _grass: MutableList<Grass> = mutableListOf(
        Grass(TOP, false), Grass(TOP, false), Grass(TOP, false),
        Grass(MIDDLE, false), Grass(MIDDLE, false), Grass(MIDDLE, false),
        Grass(BOTTOM, false), Grass(BOTTOM, false), Grass(BOTTOM, false),
    )
    val grass: List<Grass>
        get() {
            convertRateToCount()
            return _grass.toList()
        }

    private fun convertRateToCount() {
        val grassCount = progressRate / RATE_PER_UNIT

        _grass.reversed().subList(FIRST, grassCount).forEachIndexed { index, _ ->
            _grass[index] = _grass[index].copy(grassState = true)
        }
    }

    companion object {
        private const val FIRST = 0
        private const val FULL_CONDITION_RATE = 100
        private const val FULL_CONDITION_GRASS_COUNT = 9
        private const val RATE_PER_UNIT = FULL_CONDITION_RATE / FULL_CONDITION_GRASS_COUNT
    }
}
