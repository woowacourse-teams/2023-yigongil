package com.created.team201.data.datasource.remote.login

import com.created.team201.data.remote.api.AuthService
import com.created.team201.data.remote.response.AuthResponseDto
import com.created.team201.data.remote.response.IsOnBoardingDoneResponseDto

class AuthDataSourceImpl(
    private val authService: AuthService,
) : AuthDataSource {
    override suspend fun getTokens(token: String): AuthResponseDto {
        return authService.getTokens(token)
    }

    override suspend fun getLoginValidity(token: String) {
        authService.getLoginValidity(token)
    }

    override suspend fun getRenewedAccessToken(token: String): AuthResponseDto {
        return authService.postRenewedAccessToken(token)
    }


    override suspend fun getIsOnboardingDone(): IsOnBoardingDoneResponseDto {
        return authService.getIsOnboardingDone()
    }
}
