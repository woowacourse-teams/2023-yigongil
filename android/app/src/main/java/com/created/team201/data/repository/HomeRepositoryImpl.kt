package com.created.team201.data.repository

import com.created.domain.model.Todo
import com.created.domain.model.UserInfo
import com.created.domain.repository.HomeRepository
import com.created.team201.data.datasource.remote.HomeDataSource
import com.created.team201.data.mapper.toDomain
import com.created.team201.data.mapper.toTodoUpdateRequestBody
import com.created.team201.data.remote.request.NecessaryTodoRequestDto

class HomeRepositoryImpl(
    private val homeDataSource: HomeDataSource,
) : HomeRepository {

    override suspend fun getUserStudies(): UserInfo {
        return homeDataSource.getUserStudies().toDomain()
    }

    override suspend fun patchNecessaryTodo(roundId: Int, isDone: Boolean): Result<Unit> =
        runCatching {
            homeDataSource.patchNecessaryTodo(roundId, NecessaryTodoRequestDto(isDone))
        }

    override suspend fun patchOptionalTodo(todo: Todo, roundId: Int): Result<Unit> =
        runCatching {
            homeDataSource.patchOptionalTodo(
                roundId,
                todo.todoId,
                todo.toTodoUpdateRequestBody()
            )
        }

}
