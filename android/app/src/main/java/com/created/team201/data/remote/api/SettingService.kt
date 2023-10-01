package com.created.team201.data.remote.api

import retrofit2.http.DELETE

interface SettingService {
    @DELETE("v1/members")
    suspend fun requestWithdrawAccount()
}
