package com.created.team201.data.remote.api

import com.created.team201.data.remote.response.StudySummaryResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface StudyListService {

    @GET("/v1/studies/recruiting?")
    suspend fun getStudyList(
        @Query("page") index: Int,
    ): List<StudySummaryResponseDto>

    @GET("/v1/studies/recruiting/search")
    suspend fun getSearchedStudyList(
        @Query("q") searchWord: String,
        @Query("page") index: Int,
    ): List<StudySummaryResponseDto>
}
