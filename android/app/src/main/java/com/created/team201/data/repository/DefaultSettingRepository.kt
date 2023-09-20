package com.created.team201.data.repository

import com.created.domain.repository.SettingRepository
import com.created.team201.data.datasource.local.OnBoardingDataSource
import com.created.team201.data.datasource.local.TokenDataSource
import com.created.team201.data.remote.api.SettingService
import javax.inject.Inject

class DefaultSettingRepository @Inject constructor(
    private val onBoardingDataSource: OnBoardingDataSource,
    private val settingService: SettingService,
    private val tokenDataSource: TokenDataSource,
) : SettingRepository {
    override fun logout() {
        tokenDataSource.setAccessToken("")
    }

    override suspend fun requestWithdrawAccount(): Result<Unit> {
        return runCatching {
            settingService.requestWithdrawAccount()
            tokenDataSource.setAccessToken("")
            onBoardingDataSource.setOnBoardingIsDone(false)
        }
    }
}
