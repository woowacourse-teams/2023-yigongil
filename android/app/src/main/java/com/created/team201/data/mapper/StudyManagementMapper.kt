package com.created.team201.data.mapper

import com.created.domain.model.CreateTodo
import com.created.domain.model.Role
import com.created.domain.model.RoundDetail
import com.created.domain.model.StudyMember
import com.created.domain.model.Todo
import com.created.team201.data.remote.request.TodoCreateRequestDto
import com.created.team201.data.remote.request.TodoIsDoneRequestDto
import com.created.team201.data.remote.request.TodoUpdateRequestDto
import com.created.team201.data.remote.response.RoundDetailResponseDto
import com.created.team201.data.remote.response.StudyMemberResponseDto

fun RoundDetailResponseDto.toDomain(): RoundDetail = RoundDetail(
    id = id,
    masterId = masterId,
    role = Role.valueOf(role),
    necessaryTodo = necessaryTodo.toDomain(),
    optionalTodos = optionalTodos.map { it.toDomain() },
    members = members.map { it.toDomain() },

    )

fun StudyMemberResponseDto.toDomain(): StudyMember = StudyMember(
    id = id,
    nickname = nickname ?: "",
    profileImageUrl = profileImageUrl ?: "",
    isDone = isDone,
    isDeleted = isDeleted,
)

fun CreateTodo.toRequestBody(): TodoCreateRequestDto = TodoCreateRequestDto(
    content = content,
)

fun Todo.toTodoUpdateRequestBody(): TodoUpdateRequestDto = TodoUpdateRequestDto(
    isDone = isDone,
    content = content!!,
)

fun Todo.toTodoIsDoneRequestBody(): TodoIsDoneRequestDto = TodoIsDoneRequestDto(
    isDone = isDone,
)
