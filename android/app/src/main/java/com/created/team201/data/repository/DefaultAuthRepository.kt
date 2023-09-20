package com.created.team201.data.repository

import com.created.domain.repository.AuthRepository
import com.created.team201.data.datasource.local.TokenDataSource
import com.created.team201.data.remote.api.AuthService
import com.created.team201.data.remote.request.RenewedAccessTokenRequestDTO
import javax.inject.Inject

class DefaultAuthRepository @Inject constructor(
    private val authService: AuthService,
    private val tokenDataSource: TokenDataSource,
) : AuthRepository {
    override val accessToken get() = tokenDataSource.getAccessToken()
    override val refreshToken get() = tokenDataSource.getRefreshToken()


    override suspend fun requestSignUp(token: String): Result<Unit> {
        return runCatching {
            val tokens = authService.getTokens(token)
            tokenDataSource.setAccessToken(tokens.accessToken)
            tokenDataSource.setRefreshToken(tokens.accessToken)
        }
    }

    override suspend fun requestSignIn(): Result<Unit> {
        return runCatching {
            authService.getLoginValidity(accessToken)
        }
    }

    override suspend fun renewAccessToken() {
        runCatching {
            authService.postRenewedAccessToken(
                RenewedAccessTokenRequestDTO(
                    refreshToken
                )
            )
        }.onSuccess { tokens ->
            tokenDataSource.setAccessToken(tokens.accessToken)
            tokenDataSource.setRefreshToken(tokens.refreshToken)
        }.onFailure {
        }
    }
}
