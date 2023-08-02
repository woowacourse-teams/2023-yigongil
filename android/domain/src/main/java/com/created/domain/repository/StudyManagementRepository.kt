package com.created.domain.repository

import com.created.domain.model.CreateTodo
import com.created.domain.model.RoundDetail
import com.created.domain.model.StudyDetail
import com.created.domain.model.Todo

interface StudyManagementRepository {

    suspend fun getStudyDetail(studyId: Long): StudyDetail

    suspend fun getRoundDetail(studyId: Long, roundId: Long): RoundDetail

    suspend fun patchTodo(studyId: Long, todo: Todo, isNecessary: Boolean)

    suspend fun createTodo(studyId: Long, createTodo: CreateTodo): Result<Long>
}
