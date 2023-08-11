package com.created.domain.repository

import com.created.domain.model.Todo
import com.created.domain.model.UserInfo

interface HomeRepository {

    suspend fun getUserStudies(): UserInfo

    suspend fun patchNecessaryTodo(roundId: Int, isDone: Boolean): Result<Unit>

    suspend fun patchOptionalTodo(todo: Todo, roundId: Int): Result<Unit>
}
