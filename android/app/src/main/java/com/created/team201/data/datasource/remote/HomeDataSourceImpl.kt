package com.created.team201.data.datasource.remote

import com.created.team201.data.remote.api.HomeService
import com.created.team201.data.remote.request.NecessaryTodoRequestDto
import com.created.team201.data.remote.request.TodoUpdateRequestDto
import com.created.team201.data.remote.response.UserStudiesResponseDto

class HomeDataSourceImpl(
    private val homeService: HomeService,
) : HomeDataSource {
    override suspend fun getUserStudies(): UserStudiesResponseDto {
        return homeService.getUserStudies()
    }

    override suspend fun patchNecessaryTodo(
        roundId: Int,
        necessaryTodoRequestDto: NecessaryTodoRequestDto
    ) {
        homeService.patchNecessaryTodo(roundId, necessaryTodoRequestDto)
    }

    override suspend fun patchOptionalTodo(
        roundId: Int,
        todoId: Long,
        todoUpdateRequestDto: TodoUpdateRequestDto
    ) {
        homeService.patchOptionalTodo(roundId, todoId, todoUpdateRequestDto)
    }
}
