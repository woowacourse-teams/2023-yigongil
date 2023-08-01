package com.created.team201.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileService {
    @GET("/v1/members/{userId}")
    suspend fun getProfile(
        @Path("userId") userId: Long,
    )
}
