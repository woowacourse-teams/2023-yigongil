package com.created.team201.data.remote.api

import com.created.team201.data.remote.response.MustDoCertificationResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ThreadService {

    @GET("v1/studies/{id}/certifications")
    suspend fun getMustDoCertification(
        @Path("id") studyId: Long,
    ): MustDoCertificationResponseDto
}