package com.created.team201.data.repository

import com.created.domain.repository.SettingRepository
import com.created.team201.data.datasource.local.TokenDataSource
import com.created.team201.data.datasource.remote.SettingDataSource

class SettingRepositoryImpl(
    private val settingDataSource: SettingDataSource,
    private val tokenDataSource: TokenDataSource
) : SettingRepository {
    override fun logout() {
        tokenDataSource.setAccessToken("")
    }

    override suspend fun requestWithdrawAccount(): Result<Unit> {
        return runCatching {
            settingDataSource.requestWithdrawAccount()
            tokenDataSource.setAccessToken("")
        }
    }
}
