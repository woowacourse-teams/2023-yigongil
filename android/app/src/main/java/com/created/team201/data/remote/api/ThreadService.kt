package com.created.team201.data.remote.api

import com.created.team201.data.remote.request.FeedRequestDto
import com.created.team201.data.remote.response.FeedsResponseDto
import com.created.team201.data.remote.response.MustDoCertificationResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ThreadService {

    @GET("v1/studies/{id}/certifications")
    suspend fun getMustDoCertification(
        @Path("id") studyId: Long,
    ): MustDoCertificationResponseDto

    @POST("v1/studies/{id}/feeds")
    suspend fun postFeeds(
        @Path("id") studyId: Long,
        @Body content: FeedRequestDto
    ): Response<Unit>

    @GET("v1/studies/{id}/feeds")
    suspend fun getFeeds(
        @Path("id") studyId: Long,
    ): List<FeedsResponseDto>
}

