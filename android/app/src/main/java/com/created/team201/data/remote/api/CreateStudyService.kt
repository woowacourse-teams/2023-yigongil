package com.created.team201.data.remote.api

import com.created.team201.data.remote.request.CreateStudyRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CreateStudyService {
    @POST("/v1/studies")
    suspend fun createStudy(@Body createStudy: CreateStudyRequestDto): Response<Unit>

    @PUT("/v1/studies/{studyId}")
    suspend fun editStudy(
        @Path("studyId") studyId: Long,
        @Body createStudy: CreateStudyRequestDto,
    )
}
