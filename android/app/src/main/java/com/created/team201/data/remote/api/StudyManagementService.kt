package com.created.team201.data.remote.api

import com.created.team201.data.remote.request.TodoCreateRequestDto
import com.created.team201.data.remote.request.TodoRequestDto
import com.created.team201.data.remote.request.TodoUpdateRequestDto
import com.created.team201.data.remote.response.RoundDetailResponseDto
import com.created.team201.data.remote.response.StudyDetailResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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
        @Path("studyId") studyId: Long,
        @Path("todoId") todoId: Long,
        @Body todoRequestDto: TodoRequestDto,
    )

    @POST("/v1/rounds/{roundId}/todos/necessary")
    suspend fun createNecessaryTodo(
        @Path("roundId") roundId: Long,
        @Body todoCreateRequestDto: TodoCreateRequestDto,
    ): Response<Unit>

    @POST("/v1/rounds/{roundId}/todos/optional")
    suspend fun createOptionalTodo(
        @Path("roundId") roundId: Long,
        @Body todoCreateRequestDto: TodoCreateRequestDto,
    ): Response<Unit>

    @PATCH("/v1/rounds/{roundId}/todos/necessary")
    suspend fun patchNecessaryTodo(
        @Path("roundId") roundId: Long,
        @Body todoUpdateRequestDto: TodoUpdateRequestDto,
    ): Response<Unit?>

    @PATCH("/v1/rounds/{roundId}/todos/optional/{todoId}")
    suspend fun patchOptionalTodo(
        @Path("roundId") roundId: Long,
        @Path("todoId") todoId: Long,
        @Body todoUpdateRequestDto: TodoUpdateRequestDto,
    ): Response<Unit?>

    @DELETE("/v1/rounds/{roundId}/todos/optional/{todoId}")
    suspend fun deleteOptionalTodo(
        @Path("roundId") roundId: Long,
        @Path("todoId") todoId: Long,
    )
}
