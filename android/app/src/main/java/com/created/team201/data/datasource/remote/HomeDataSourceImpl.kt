package com.created.team201.data.datasource.remote

import com.created.team201.data.remote.api.HomeService
import com.created.team201.data.remote.request.TodoRequestDto
import com.created.team201.data.remote.response.UserStudiesResponseDto

class HomeDataSourceImpl(
    private val homeService: HomeService,
) : HomeDataSource {
    override suspend fun getUserStudies(): UserStudiesResponseDto {
        return homeService.getUserStudies()
    }

    override suspend fun patchTodo(studyId: Int, todoId: Int, todoRequestDto: TodoRequestDto) {
        homeService.patchTodo(
            studyId = studyId,
            todoId = todoId,
            todoRequestDto = todoRequestDto,
        )
    }
}
