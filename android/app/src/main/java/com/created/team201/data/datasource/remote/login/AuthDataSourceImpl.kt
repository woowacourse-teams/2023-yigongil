package com.created.team201.data.datasource.remote.login

import com.created.team201.data.remote.api.AuthService
import com.created.team201.data.remote.response.AuthResponseDto

class AuthDataSourceImpl(
    private val authService: AuthService,
) : AuthDataSource {
    override suspend fun getTokens(token: String): AuthResponseDto {
        return authService.getTokens(token)
    }

    override suspend fun getLoginValidity() {
        authService.getLoginValidity()
    }

    override suspend fun getRenewedAccessToken(token: String): AuthResponseDto {
        return authService.getRenewedAccessToken(token)
    }
}
