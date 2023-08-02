package com.created.domain.repository

interface AuthRepository {

    suspend fun signUp(token: String): Result<Unit>
}
