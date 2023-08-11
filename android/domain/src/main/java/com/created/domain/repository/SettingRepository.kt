package com.created.domain.repository

interface SettingRepository {
    fun logout()

    suspend fun requestWithdrawAccount(): Result<Unit>
}
