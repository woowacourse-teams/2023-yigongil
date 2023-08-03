package com.created.team201.data.remote.interceptor

import com.created.team201.data.datasource.local.TokenDataSource
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(
    private val tokenDataSource: TokenDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val urlEncodedPath = originalRequest.url.encodedPath

        val newRequest = when (containAuthInUrl(urlEncodedPath)) {
            true -> originalRequest
            false -> buildNewRequest(originalRequest)
        }

        val response = chain.proceed(newRequest)
        // 특정 API를 제외하고, 모든 헤더에 토큰 넣어주기

        if (response.code == TOKEN_INVALID) {
//              만약에 토큰이 만료가 되어서 401이 날아올 경우, 토큰 재발급 후 다시 리퀘스트 만들어서 넣기
//                val newAccessToken = response.body
//                val newRequestWithNewToken = originalRequest.newBuilder()
//                    .addHeader(HEADER_KEY, newAccessToken.toString())
//                    .build()
//                response.close()
//                val newResponse = chain.proceed(newRequestWithNewToken)
//                return newResponse
        }

        return response
    }

    private suspend fun getAccessToken(): String {
        return tokenDataSource.getAccessToken()
    }

    private suspend fun buildNewRequest(
        originalRequest: Request,
    ) = originalRequest.newBuilder().addHeader(
        HEADER_KEY,
        getAccessToken(),
    ).build()

    private fun containAuthInUrl(url: String): Boolean = url.contains(LOGIN)

    companion object {
        private const val LOGIN = "v1/login"
        private const val HEADER_KEY = "Authorization"
        private const val TOKEN_INVALID = 401
    }
}
