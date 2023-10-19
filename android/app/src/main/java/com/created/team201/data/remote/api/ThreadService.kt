package com.created.team201.data.remote.api

import com.created.team201.data.remote.request.FeedRequestDto
import com.created.team201.data.remote.request.MustDoContentRequestDto
import com.created.team201.data.remote.response.FeedsResponseDto
import com.created.team201.data.remote.response.MustDoCertificationResponseDto
import com.created.team201.data.remote.response.WeeklyMustDoResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ThreadService {

    @GET("v1/studies/{id}/certifications")
    suspend fun getMustDoCertification(
        @Path("id") studyId: Long,
    ): MustDoCertificationResponseDto

    @POST("v1/studies/{id}/feeds")
    suspend fun postFeeds(
        @Path("id") studyId: Long,
        @Body content: FeedRequestDto,
    ): Response<Unit>

    @GET("v1/studies/{id}/feeds")
    suspend fun getFeeds(
        @Path("id") studyId: Long,
    ): List<FeedsResponseDto>

    @GET("v1/studies/{studyId}/rounds?")
    suspend fun getMustDos(
        @Path("studyId") studyId: Long,
        @Query("weekNumber") weekNumber: Int,
    ): List<WeeklyMustDoResponseDto>

    @PUT("v1/rounds/{roundId}/todos")
    suspend fun putMustDo(
        @Path("roundId") roundId: Long,
        @Body content: MustDoContentRequestDto,
    ): Response<Unit>

    @PATCH("v1/studies/{studyId}/end")
    suspend fun endStudy(
        @Path("studyId") studyId: Long,
    ): Response<Unit>

    @DELETE("v1/studies/{studyId}/exit")
    suspend fun quitStudy(
        @Path("studyId") studyId: Long,
    ): Response<Unit>
}
