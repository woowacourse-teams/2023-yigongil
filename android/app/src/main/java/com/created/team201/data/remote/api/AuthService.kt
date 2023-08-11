package com.created.team201.data.remote.api

import com.created.team201.data.remote.response.AuthResponseDto
import com.created.team201.data.remote.response.IsOnBoardingDoneResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @GET("/v1/login/github/tokens?")
    suspend fun getTokens(
        @Query("code") oauthToken: String,
    ): AuthResponseDto

    @POST("/v1/login/github/renew")
    suspend fun postRenewedAccessToken(
        @Query("accessToken") accessToken: String,
    ): AuthResponseDto

    @GET("/v1/home")
    suspend fun getLoginValidity(
        @Header("Authorization") accessToken: String,
    )
}
