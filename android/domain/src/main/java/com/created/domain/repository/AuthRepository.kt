package com.created.domain.repository

interface AuthRepository {

    val accessToken: String

    suspend fun requestSignUp(token: String): Result<Unit>

    suspend fun requestSignIn(): Result<Unit>

    suspend fun renewAccessToken()
}
