package com.created.domain.repository

import com.created.domain.model.AppUpdateInformation

interface SplashRepository {
    fun getAppUpdateInformation(versionCode: Int, onAppUpdateInformationEvent: (AppUpdateInformation) -> Unit)
}
