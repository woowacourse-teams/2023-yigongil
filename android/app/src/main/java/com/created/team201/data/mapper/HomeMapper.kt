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
    isDone = this.isDone,
    content = this.content,
)

fun TodoUiModel.toDomain(): Todo = Todo(
    todoId = this.todoId,
    content = this.content,
    isDone = this.isDone,
)

fun UserStudiesResponseDto.toDomain(): UserInfo = UserInfo(
    userName = this.nickname,
    myId = this.myId,
    studies = this.studies.map { it.toDomain() },
)

fun StudyResponseDto.toDomain(): Study = Study(
    studyName = this.name,
    studyId = this.id,
    progressRate = this.membersNecessaryTodoProgressRate,
    leftDays = this.leftDays,
    necessaryTodo = this.necessaryTodo.toDomain(),
    nextDate = this.nextDate,
    optionalTodo = this.optionalTodos.map { it.toDomain() },
)

fun TodoResponseDto.toDomain(): Todo = Todo(
    todoId = this.id,
    content = this.content,
    isDone = this.isDone,
)
