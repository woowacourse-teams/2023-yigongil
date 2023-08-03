package com.created.team201.data.datasource.remote.login

import com.created.team201.data.remote.response.AuthResponseDto

interface AuthDataSource {
    suspend fun getTokens(token: String): AuthResponseDto

    suspend fun getLoginValidity()

    suspend fun getRenewedAccessToken(token: String): AuthResponseDto
}
