package com.created.team201.data.remote.api

import com.created.team201.data.remote.request.TodoCreateRequestDto
import com.created.team201.data.remote.request.TodoRequestDto
import com.created.team201.data.remote.response.RoundDetailResponseDto
import com.created.team201.data.remote.response.StudyDetailResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface StudyManagementService {
    @GET("/v1/studies/{studyId}")
    suspend fun getStudyDetail(
        @Path("studyId") studyId: Long,
    ): StudyDetailResponseDto

    @GET("/v1/studies/{studyId}/rounds/{roundId}")
    suspend fun getRoundDetail(
        @Path("studyId") studyId: Long,
        @Path("roundId") roundId: Long,
    ): RoundDetailResponseDto

    @PATCH("/v1/studies/{studyId}/todos/{todoId}")
    suspend fun patchTodo(
        @Path("studyId") studyId: Int,
        @Path("todoId") todoId: Long,
        @Body todoRequestDto: TodoRequestDto,
    )

    @POST("/v1/studies/{studyId}/todos")
    suspend fun createTodo(
        @Path("studyId") studyId: Int,
        @Body todoCreateRequestDto: TodoCreateRequestDto,
    )
}
