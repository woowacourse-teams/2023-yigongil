package com.created.team201.data.remote.api

import retrofit2.http.DELETE

interface SettingService {
    @DELETE("")
    suspend fun requestWithdrawAccount()
}
