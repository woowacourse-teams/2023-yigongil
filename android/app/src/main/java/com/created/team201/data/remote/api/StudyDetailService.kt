package com.created.team201.data.remote.api

import com.created.team201.data.remote.response.StudyDetailResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface StudyDetailService {
    @GET("/v1/studies/{studyId}")
    suspend fun getStudyDetail(
        @Path("studyId") studyId: Long,
    ): StudyDetailResponseDto
}
