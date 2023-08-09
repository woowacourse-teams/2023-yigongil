package com.created.team201.data.repository

import com.created.domain.model.CreateTodo
import com.created.domain.model.RoundDetail
import com.created.domain.model.StudyDetail
import com.created.domain.model.Todo
import com.created.domain.repository.StudyManagementRepository
import com.created.team201.data.datasource.remote.StudyManagementDataSource
import com.created.team201.data.mapper.toDomain
import com.created.team201.data.mapper.toRequestBody
import com.created.team201.data.mapper.toTodoUpdateRequestBody

class StudyManagementRepositoryImpl(
    private val studyManagementDataSource: StudyManagementDataSource,
) : StudyManagementRepository {
    override suspend fun getStudyDetail(studyId: Long): StudyDetail {
        return studyManagementDataSource.getStudyDetail(studyId).toDomain()
    }

    override suspend fun getRoundDetail(studyId: Long, roundId: Long): RoundDetail {
        return studyManagementDataSource.getRoundDetail(studyId, roundId).toDomain()
    }

    override suspend fun patchTodo(studyId: Long, todo: Todo, isNecessary: Boolean) {
        studyManagementDataSource.patchTodo(
            studyId,
            todo.todoId,
            todo.toRequestBody(isNecessary),
        )
    }

    override suspend fun createNecessaryTodo(roundId: Long, createTodo: CreateTodo): Result<Long> {
        return runCatching {
            val responseHeader =
                studyManagementDataSource.createNecessaryTodo(roundId, createTodo.toRequestBody())
                    .headers()

            responseHeader[KEY_LOCATION]?.split(KEY_SLASH)?.last()?.toLong()
                ?: throw IllegalStateException("헤더를 찾을 수 없습니다.")
        }
    }

    override suspend fun createOptionalTodo(roundId: Long, createTodo: CreateTodo): Result<Long> {
        return runCatching {
            val responseHeader =
                studyManagementDataSource.createOptionalTodo(roundId, createTodo.toRequestBody())
                    .headers()

            responseHeader[KEY_LOCATION]?.split(KEY_SLASH)?.last()?.toLong()
                ?: throw IllegalStateException("헤더를 찾을 수 없습니다.")
        }
    }

    override suspend fun patchNecessaryTodo(roundId: Long, todo: Todo) {
        studyManagementDataSource.patchNecessaryTodo(roundId, todo.toTodoUpdateRequestBody())
    }

    override suspend fun patchOptionalTodo(roundId: Long, todo: Todo) {
        studyManagementDataSource.patchOptionalTodo(
            roundId,
            todo.todoId,
            todo.toTodoUpdateRequestBody(),
        )
    }

    override suspend fun deleteOptionalTodo(roundId: Long, todoId: Long) {
        studyManagementDataSource.deleteOptionalTodo(roundId, todoId)
    }

    companion object {
        private const val KEY_LOCATION = "Location"
        private const val KEY_SLASH = "/"
    }
}
