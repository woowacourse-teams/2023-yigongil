package com.created.team201.data.datasource.remote

import com.created.team201.data.remote.request.NecessaryTodoRequestDto
import com.created.team201.data.remote.request.TodoUpdateRequestDto
import com.created.team201.data.remote.response.UserStudiesResponseDto

interface HomeDataSource {

    suspend fun getUserStudies(): UserStudiesResponseDto

    suspend fun patchNecessaryTodo(roundId: Int, necessaryTodoRequestDto: NecessaryTodoRequestDto)

    suspend fun patchOptionalTodo(
        roundId: Int,
        todoId: Long,
        todoUpdateRequestDto: TodoUpdateRequestDto
    )
}
