package com.created.team201.data.repository

import com.created.team201.data.datasource.local.OnBoardingDataSource
import com.created.team201.data.datasource.local.TokenDataSource
import com.created.team201.data.remote.api.SettingService
import javax.inject.Inject

class SettingRepository @Inject constructor(
    private val onBoardingDataSource: OnBoardingDataSource,
    private val settingService: SettingService,
    private val tokenDataSource: TokenDataSource,
) {

    fun logout() {
        tokenDataSource.setAccessToken("")
        tokenDataSource.setRefreshToken("")
        onBoardingDataSource.setOnBoardingIsDone(false)
    }

    suspend fun requestWithdrawAccount(): Result<Unit> {
        return runCatching {
            settingService.requestWithdrawAccount()
            tokenDataSource.setAccessToken("")
            tokenDataSource.setRefreshToken("")
            onBoardingDataSource.setOnBoardingIsDone(false)
        }
    }
}
