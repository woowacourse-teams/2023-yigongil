package com.created.team201.data.datasource.local

class TokenDataSourceImpl(
    private val tokenStorage: TokenStorage,
) : TokenDataSource {

    override fun getAccessToken(): String {
        return tokenStorage.fetchToken(ACCESS_TOKEN) ?: throw IllegalStateException()
    }

    override fun setAccessToken(token: String) {
        tokenStorage.putToken(ACCESS_TOKEN, token)
    }

    companion object {
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
    }
}
