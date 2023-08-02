package com.created.team201.data.repository

import com.created.domain.model.CreateTodo
import com.created.domain.model.RoundDetail
import com.created.domain.model.StudyDetail
import com.created.domain.model.Todo
import com.created.domain.repository.StudyManagementRepository
import com.created.team201.data.datasource.remote.StudyManagementDataSource
import com.created.team201.data.mapper.toDomain
import com.created.team201.data.mapper.toRequestBody

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

    override suspend fun createTodo(studyId: Long, createTodo: CreateTodo) {
        studyManagementDataSource.createTodo(studyId, createTodo.toRequestBody())
    }
}
