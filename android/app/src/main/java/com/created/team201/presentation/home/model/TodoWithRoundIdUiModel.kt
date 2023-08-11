package com.created.team201.presentation.home.model

data class TodoWithRoundIdUiModel(
    val todoId: Long,
    val content: String?,
    val isDone: Boolean,
    val roundId: Int,
) {
    fun isContentEmpty(): Boolean = !content.isNullOrEmpty()
}
