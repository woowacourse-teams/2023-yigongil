package com.created.team201.data.repository

import com.created.domain.model.OnBoarding
import com.created.domain.repository.OnBoardingRepository
import com.created.team201.data.datasource.remote.OnBoardingDataSource

class OnBoardingRepositoryImpl(
    private val onBoardingDataSource: OnBoardingDataSource
) : OnBoardingRepository {
    override suspend fun patchOnBoarding(onBoarding: OnBoarding): Result<String> {
        return runCatching {
            onBoardingDataSource.patchOnBoarding(onBoarding).message
        }
    }
}
