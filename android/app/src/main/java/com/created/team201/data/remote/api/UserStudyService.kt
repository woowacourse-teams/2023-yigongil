package com.created.team201.data.remote.api

import com.created.team201.data.remote.response.UserStudyResponseDto
import retrofit2.http.GET

interface UserStudyService {

    @GET("v1/home")
    suspend fun getUserStudies(): List<UserStudyResponseDto>
}
