package com.created.team201.data.mapper

import com.created.domain.model.Study
import com.created.domain.model.Todo
import com.created.domain.model.UserInfo
import com.created.team201.data.remote.request.TodoRequestDto
import com.created.team201.data.remote.response.StudyResponseDto
import com.created.team201.data.remote.response.TodoResponseDto
import com.created.team201.data.remote.response.UserStudiesResponseDto
import com.created.team201.presentation.home.model.TodoUiModel

fun Todo.toRequestBody(isNecessary: Boolean): TodoRequestDto = TodoRequestDto(
    isNecessary = isNecessary,
    isDone = isDone,
    content = content,
)

fun TodoUiModel.toDomain(): Todo = Todo(
    todoId = todoId,
    content = content,
    isDone = isDone,
)

fun UserStudiesResponseDto.toDomain(): UserInfo = UserInfo(
    userName = nickname,
    myId = myId,
    studies = studies.map { it.toDomain() },
)

fun StudyResponseDto.toDomain(): Study = Study(
    studyName = name,
    studyId = id,
    progressRate = membersNecessaryTodoProgressRate,
    leftDays = leftDays,
    necessaryTodo = necessaryTodo.toDomain(),
    nextDate = nextDate,
    optionalTodo = optionalTodos.map { it.toDomain() },
)

fun TodoResponseDto.toDomain(): Todo = Todo(
    todoId = id,
    content = content,
    isDone = isDone,
)
