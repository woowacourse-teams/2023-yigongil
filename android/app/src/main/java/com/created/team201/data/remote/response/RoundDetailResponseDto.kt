package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoundDetailResponseDto(
    @SerialName("id")
    val id: Long,
    @SerialName("masterId")
    val masterId: Long,
    @SerialName("role")
    val role: Int,
    @SerialName("necessaryTodo")
    val necessaryTodo: TodoResponseDto,
    @SerialName("optionalTodos")
    val optionalTodos: List<TodoResponseDto>,
    @SerialName("members")
    val members: List<StudyMemberResponseDto>,
)
