package com.created.team201.data.remote.api

import com.created.team201.data.remote.response.StudySummaryResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CommonStudyListService {

    @GET("v1/studies?")
    suspend fun getStudyList(
        @Query("status") status: String?,
        @Query("page") index: Int?,
        @Query("search") searchWord: String?,
    ): List<StudySummaryResponseDto>
}
