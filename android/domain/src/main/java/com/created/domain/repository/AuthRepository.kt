package com.created.domain.repository

interface AuthRepository {

    suspend fun requestSignUp(token: String): Result<Unit>

    suspend fun requestSignIn(): Result<Unit>
}
