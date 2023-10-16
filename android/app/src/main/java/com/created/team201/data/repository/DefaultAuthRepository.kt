package com.created.team201.data.repository

import com.created.domain.model.response.NetworkResponse
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
            tokenDataSource.setRefreshToken(tokens.refreshToken)
        }
    }

    override suspend fun requestSignIn(): NetworkResponse<Unit> {
        return authService.getLoginValidity(accessToken)
    }

    override suspend fun renewAccessToken(): NetworkResponse<Unit> {
        val response = authService.postRenewedAccessToken(
            RenewedAccessTokenRequestDTO(
                refreshToken
            )
        )
        return when (response) {
            is NetworkResponse.Success -> {
                val tokens = response.body
                tokenDataSource.setAccessToken(tokens.accessToken)
                tokenDataSource.setRefreshToken(tokens.refreshToken)
                NetworkResponse.Success(Unit)
            }

            is NetworkResponse.Failure -> response
            is NetworkResponse.NetworkError -> response
            is NetworkResponse.Unexpected -> response
        }
    }
}
