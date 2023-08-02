package com.created.team201.data.datasource.remote

import com.created.team201.data.remote.request.TodoCreateRequestDto
import com.created.team201.data.remote.request.TodoRequestDto
import com.created.team201.data.remote.response.RoundDetailResponseDto
import com.created.team201.data.remote.response.StudyDetailResponseDto

interface StudyManagementDataSource {

    suspend fun getStudyDetail(studyId: Long): StudyDetailResponseDto

    suspend fun getRoundDetail(studyId: Long, roundId: Long): RoundDetailResponseDto

    suspend fun patchTodo(studyId: Long, todoId: Long, todoRequestDto: TodoRequestDto)

    suspend fun createTodo(studyId: Long, todoCreateRequestDto: TodoCreateRequestDto)
}
