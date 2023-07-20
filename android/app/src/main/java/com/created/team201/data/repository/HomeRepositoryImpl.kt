package com.created.team201.data.repository

import com.created.domain.model.Todo
import com.created.domain.model.UserInfo
import com.created.domain.repository.HomeRepository
import com.created.team201.data.datasource.remote.HomeDataSource
import com.created.team201.data.mapper.toDomain
import com.created.team201.data.mapper.toRequestBody

class HomeRepositoryImpl(
    private val homeDataSource: HomeDataSource,
) : HomeRepository {
    override suspend fun getUserStudies(): UserInfo {
        return homeDataSource.getUserStudies().toDomain()
    }

    override suspend fun patchTodo(todo: Todo, studyId: Long, isNecessary: Boolean) {
        homeDataSource.patchTodo(
            studyId = studyId.toInt(),
            todoId = todo.todoId,
            todoRequestDto = todo.toRequestBody(isNecessary),
        )
    }
}
