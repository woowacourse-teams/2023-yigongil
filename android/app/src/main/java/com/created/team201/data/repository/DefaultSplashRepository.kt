package com.created.team201.data.repository

import com.created.domain.model.AppUpdateInformation
import com.created.domain.model.response.NetworkResponse
import com.created.domain.repository.SplashRepository
import com.created.team201.data.mapper.toDomain
import com.created.team201.data.remote.api.SplashService
import javax.inject.Inject

class DefaultSplashRepository @Inject constructor(
    private val splashService: SplashService,
) : SplashRepository {
    override suspend fun getAppUpdateInformation(versionCode: Int): NetworkResponse<AppUpdateInformation> {
        return when (val response = splashService.getAppUpdateInformation(versionCode)) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.body.toDomain())
            is NetworkResponse.Failure -> NetworkResponse.Failure(response.responseCode, response.error)
            is NetworkResponse.NetworkError -> NetworkResponse.NetworkError(response.exception)
            is NetworkResponse.Unexpected -> NetworkResponse.Unexpected(response.t)
        }
    }
}
