package com.created.team201.data.datasource.local

interface TokenDataSource {
    fun getIsGuest(): Boolean

    fun getAccessToken(): String

    fun getRefreshToken(): String

    fun setAccessToken(token: String)

    fun setRefreshToken(token: String)
}
