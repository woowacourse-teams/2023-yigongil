package com.created.team201.data.remote.api

import com.created.team201.data.remote.response.StudySummaryResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MemberStudyListService {

    @GET("v1/studies/created")
    suspend fun getCreatedStudyList(): List<StudySummaryResponseDto>

    @GET("v1/studies/waiting?")
    suspend fun getAppliedStudyList(
        @Query("search") searchWord: String?,
        @Query("role") role: String,
    ): List<StudySummaryResponseDto>
}
