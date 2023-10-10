package com.created.domain.repository

import com.created.domain.model.response.NetworkResponse

interface AuthRepository {

    val accessToken: String
    val refreshToken: String

    suspend fun requestSignUp(token: String): Result<Unit>

    suspend fun requestSignIn(): NetworkResponse<Unit>

    suspend fun renewAccessToken(): NetworkResponse<Unit>
}
