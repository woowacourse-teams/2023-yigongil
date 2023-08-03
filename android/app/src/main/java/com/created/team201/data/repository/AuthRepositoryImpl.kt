package com.created.team201.data.repository

import android.util.Log
import com.created.domain.repository.AuthRepository
import com.created.team201.data.datasource.local.TokenDataSource
import com.created.team201.data.datasource.remote.login.AuthDataSource

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val tokenDataSource: TokenDataSource,
) : AuthRepository {
    override val accessToken get() = tokenDataSource.getAccessToken()

    override suspend fun requestSignUp(token: String): Result<Unit> {
        Log.d("123123", "125")
        return runCatching {
            val userAccessToken = authDataSource.getTokens(token).accessToken
            Log.d("123123", "125")
            tokenDataSource.setAccessToken(userAccessToken)
            Log.d("123123", "accessToken")
        }
    }

    override suspend fun requestSignIn(): Result<Unit> {
        Log.d("123123", "125")
        return runCatching {
            tokenDataSource.setAccessToken("12312d12d2d")
            Log.d("123123", accessToken)

            authDataSource.getLoginValidity()
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
