package com.created.team201.data.remote.api

import com.created.team201.data.remote.request.TodoRequestDto
import com.created.team201.data.remote.response.UserStudiesResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface HomeService {

    @GET("/v1/home")
    suspend fun getUserStudies(): UserStudiesResponseDto

    @PATCH("/v1/studies/{studyId}/todos/{todoId}")
    suspend fun patchTodo(
        @Path("studyId") studyId: Int,
        @Path("todoId") todoId: Long,
        @Body todoRequestDto: TodoRequestDto,
    )
}
