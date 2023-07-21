package com.created.domain.repository

import com.created.domain.model.Todo
import com.created.domain.model.UserInfo

interface HomeRepository {

    suspend fun getUserStudies(): UserInfo

    suspend fun patchTodo(todo: Todo, studyId: Long, isNecessary: Boolean)
}
