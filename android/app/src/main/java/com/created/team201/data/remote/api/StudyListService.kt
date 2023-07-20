package com.created.team201.data.remote.api

import com.created.team201.data.remote.response.StudySummaryResponseDto
import retrofit2.http.GET

interface StudyListService {

    @GET("/v1/studies/recruiting?page=0")
    suspend fun getStudyList(): List<StudySummaryResponseDto>
}
