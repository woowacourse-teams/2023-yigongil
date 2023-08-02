package com.created.team201.data.remote.api

import com.created.team201.data.remote.response.AuthResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthService {

    @GET("v1/login/github/tokens?")
    suspend fun getTokens(
        @Query("code") oauthToken: String,
    ): AuthResponseDto
}
