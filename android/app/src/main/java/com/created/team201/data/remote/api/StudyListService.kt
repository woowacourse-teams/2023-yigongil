package com.created.team201.data.remote.api

import com.created.team201.data.remote.response.StudySummaryResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface StudyListService {

    @GET("/v1/studies/recruiting?page={index}")
    suspend fun getStudyList(
        @Path("index") index: Int,
    ): List<StudySummaryResponseDto>
}
