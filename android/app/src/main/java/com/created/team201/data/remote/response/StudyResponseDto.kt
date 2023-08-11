package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyResponseDto(
    @SerialName("id")
    val id: Int,
    @SerialName("leftDays")
    val leftDays: Int,
    @SerialName("memberNecessaryTodoProgressRate")
    val membersNecessaryTodoProgressRate: Int,
    @SerialName("name")
    val name: String,
    @SerialName("necessaryTodo")
    val necessaryTodo: TodoResponseDto,
    @SerialName("nextDate")
    val nextDate: String,
    @SerialName("optionalTodos")
    val optionalTodos: List<TodoResponseDto>,
    @SerialName("roundId")
    val roundId: Int,
)
