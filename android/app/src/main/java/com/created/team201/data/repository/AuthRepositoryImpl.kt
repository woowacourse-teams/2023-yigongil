package com.created.team201.data.repository

import com.created.domain.repository.AuthRepository
import com.created.team201.data.datasource.local.TokenDataSource
import com.created.team201.data.datasource.remote.login.AuthDataSource

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val tokenDataSource: TokenDataSource,
) : AuthRepository {
    override val accessToken get() = tokenDataSource.getAccessToken()

    override suspend fun requestSignUp(token: String): Result<Unit> {
        return runCatching {
            val userAccessToken = authDataSource.getTokens(token).accessToken
            tokenDataSource.setAccessToken(userAccessToken)
        }
    }

    override suspend fun requestSignIn(): Result<Unit> {
        return runCatching {
            authDataSource.getLoginValidity(accessToken)
        }
    }

    override suspend fun renewAccessToken() {
        runCatching {
            authDataSource.getRenewedAccessToken(accessToken)
        }.onSuccess {
            tokenDataSource.setAccessToken(it.accessToken)
        }.onFailure {
        }
    }
}
