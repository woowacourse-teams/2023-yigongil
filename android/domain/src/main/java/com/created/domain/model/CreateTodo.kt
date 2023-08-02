package com.created.domain.model

data class CreateTodo(
    val isNecessary: Boolean,
    val roundId: Long,
    val content: String,
)
