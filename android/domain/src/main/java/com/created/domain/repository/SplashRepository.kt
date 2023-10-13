package com.created.domain.repository

import com.created.domain.model.AppUpdateInformation
import com.created.domain.model.response.NetworkResponse

interface SplashRepository {
    suspend fun getAppUpdateInformation(versionCode: Int): NetworkResponse<AppUpdateInformation>
}
