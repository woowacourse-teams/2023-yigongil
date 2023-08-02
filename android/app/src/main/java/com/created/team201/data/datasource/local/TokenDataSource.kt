package com.created.team201.data.datasource.local

interface TokenDataSource {

    suspend fun getAccessToken(): String

    suspend fun setAccessToken(token: String)
}
