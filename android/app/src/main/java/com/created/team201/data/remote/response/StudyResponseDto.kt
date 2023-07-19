package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyResponseDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("roundId")
    val roundId: Int,
    @SerialName("necessaryTodo")
    val necessaryTodo: TodoResponseDto,
    @SerialName("leftDays")
    val leftDays: Int,
    @SerialName("membersNecessaryTodoProgressRate")
    val membersNecessaryTodoProgressRate: Int,
    @SerialName("nextDate")
    val nextDate: String,
    @SerialName("optionalTodos")
    val optionalTodos: List<TodoResponseDto>,
)
