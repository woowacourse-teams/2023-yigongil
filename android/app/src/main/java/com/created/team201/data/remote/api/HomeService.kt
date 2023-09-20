package com.created.team201.data.remote.api

import com.created.team201.data.remote.response.HomeResponseDto
import retrofit2.http.GET

interface HomeService {

    @GET("/v1/home")
    suspend fun getUserStudies(): HomeResponseDto
}
