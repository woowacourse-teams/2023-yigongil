package com.created.team201.data.remote.api

import com.created.team201.data.remote.request.RenewedAccessTokenRequestDTO
import com.created.team201.data.remote.response.AuthResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @GET("/v1/login/github/tokens?")
    suspend fun getTokens(
        @Query("code") oauthToken: String,
    ): AuthResponseDto

    @POST("/v1/login/tokens/refresh")
    suspend fun postRenewedAccessToken(
        @Body refreshToken: RenewedAccessTokenRequestDTO,
    ): AuthResponseDto

    @GET("/v1/home")
    suspend fun getLoginValidity(
        @Header("Authorization") accessToken: String,
    )
}
