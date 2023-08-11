package com.created.team201.data.remote.api

import com.created.team201.data.remote.request.NecessaryTodoRequestDto
import com.created.team201.data.remote.request.TodoUpdateRequestDto
import com.created.team201.data.remote.response.UserStudiesResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface HomeService {

    @GET("/v1/home")
    suspend fun getUserStudies(): UserStudiesResponseDto

    @PATCH("/v1/rounds/{roundId}/todos/necessary")
    suspend fun patchNecessaryTodo(
        @Path("roundId") roundId: Int,
        @Body necessaryTodoRequestDto: NecessaryTodoRequestDto,
    )

    @PATCH("/v1/rounds/{roundId}/todos/optional/{todoId}")
    suspend fun patchOptionalTodo(
        @Path("roundId") roundId: Int,
        @Path("todoId") todoId: Long,
        @Body todoUpdateRequestDto: TodoUpdateRequestDto,
    )
}
