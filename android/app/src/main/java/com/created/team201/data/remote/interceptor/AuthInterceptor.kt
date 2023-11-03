package com.created.team201.data.remote.interceptor

import android.util.Log
import com.created.team201.data.repository.AuthRepository
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

        if (response.code == TOKEN_INVALID) {
            synchronized(this) {
                Log.d(LOG_AUTH_INTERCEPTOR_TAG, "Failed because Old AccessToken")
                response.close()
                return putNewHeader(chain)
            }
        }

        return response
    }

    private fun putNewHeader(chain: Interceptor.Chain): Response {
        renewAccessToken()

        val newRequestWithNewToken = chain.request().newBuilder()
            .addHeader(HEADER_KEY, authRepository.accessToken)
            .build()

        return chain.proceed(newRequestWithNewToken)
    }

    private fun renewAccessToken() = runBlocking {
        authRepository.renewAccessToken()
        Log.d(LOG_AUTH_INTERCEPTOR_TAG, "Renewed AccessToken")
    }

    companion object {
        private const val LOG_AUTH_INTERCEPTOR_TAG = "AuthInterceptor"
        private const val HEADER_KEY = "Authorization"
        private const val TOKEN_INVALID = 401
    }
}
