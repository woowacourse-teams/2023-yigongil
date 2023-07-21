package com.created.team201.data.remote.api

import com.created.team201.data.remote.response.StudyManageResponseDto
import retrofit2.http.GET

interface StudyManageService {

    @GET("/v1/studies/my")
    suspend fun getMyStudies(): List<StudyManageResponseDto>
}
