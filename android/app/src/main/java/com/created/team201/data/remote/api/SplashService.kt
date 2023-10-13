package com.created.team201.data.remote.api

import com.created.domain.model.response.NetworkResponse
import com.created.team201.data.remote.response.AppUpdateInformationResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SplashService {

    @GET("v1/check-version?")
    suspend fun getAppUpdateInformation(
        @Query("v") versionCode: Int,
    ): NetworkResponse<AppUpdateInformationResponseDto>
}
