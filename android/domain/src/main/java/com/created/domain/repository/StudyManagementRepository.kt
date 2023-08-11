package com.created.domain.repository

import com.created.domain.model.CreateTodo
import com.created.domain.model.Profile
import com.created.domain.model.RoundDetail
import com.created.domain.model.StudyDetail
import com.created.domain.model.Todo

interface StudyManagementRepository {

    suspend fun getStudyDetail(studyId: Long): StudyDetail

    suspend fun getRoundDetail(studyId: Long, roundId: Long): RoundDetail

    suspend fun patchTodo(studyId: Long, todo: Todo, isNecessary: Boolean)

    suspend fun createNecessaryTodo(roundId: Long, createTodo: CreateTodo): Result<Long>

    suspend fun createOptionalTodo(roundId: Long, createTodo: CreateTodo): Result<Long>

    suspend fun patchNecessaryTodo(roundId: Long, todo: Todo)

    suspend fun patchNecessaryTodoIsDone(roundId: Long, todo: Todo)

    suspend fun patchOptionalTodo(roundId: Long, todo: Todo)

    suspend fun deleteOptionalTodo(roundId: Long, todoId: Long)

    suspend fun getMyProfile(): Profile
}
