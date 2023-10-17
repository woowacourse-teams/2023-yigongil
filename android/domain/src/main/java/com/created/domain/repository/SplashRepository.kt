package com.created.domain.repository

import com.created.domain.model.AppUpdateInformation
import kotlinx.coroutines.flow.SharedFlow

interface SplashRepository {
    val appUpdateInformation: SharedFlow<AppUpdateInformation>

    suspend fun getAppUpdateInformation(versionCode: Int)
}
