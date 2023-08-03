package com.created.team201.data.remote.interceptor

import com.created.domain.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val authRepository: AuthRepository,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader(
                HEADER_KEY,
                authRepository.accessToken,
            ).build()

        val response = chain.proceed(newRequest)

        if (response.code == TOKEN_INVALID) return putNewHeader(response, chain)

        return response
    }

    private fun putNewHeader(response: Response, chain: Interceptor.Chain): Response {
        renewAccessToken()

        val newRequestWithNewToken = chain.request().newBuilder()
            .addHeader(HEADER_KEY, authRepository.accessToken)
            .build()

        response.close()

        return chain.proceed(newRequestWithNewToken)
    }

    private fun renewAccessToken() = runBlocking {
        authRepository.renewAccessToken()
    }

    companion object {
        private const val HEADER_KEY = "Authorization"
        private const val TOKEN_INVALID = 401
    }
}
