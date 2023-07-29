package com.created.team201.data.datasource.remote

import com.created.team201.data.remote.request.TodoRequestDto
import com.created.team201.data.remote.response.UserStudiesResponseDto

interface HomeDataSource {

    suspend fun getUserStudies(): UserStudiesResponseDto

    suspend fun patchTodo(studyId: Int, todoId: Int, todoRequestDto: TodoRequestDto)
}
