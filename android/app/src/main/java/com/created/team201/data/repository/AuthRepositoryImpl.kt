package com.created.team201.data.repository

import com.created.domain.repository.AuthRepository
import com.created.team201.data.datasource.local.TokenDataSource
import com.created.team201.data.datasource.remote.login.AuthDataSource

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val tokenDataSource: TokenDataSource,
) : AuthRepository {

    override suspend fun signUp(token: String): Result<Unit> {
        return runCatching {
            val token = authDataSource.getTokens(token)
        }
    }
}
