package com.created.team201.data.datasource.remote

import com.created.team201.data.remote.api.SettingService

class SettingDataSourceImpl(
    private val settingService: SettingService,
) : SettingDataSource {
    override suspend fun requestWithdrawAccount() {
        settingService.requestWithdrawAccount()
    }
}
