package com.created.team201.data.repository

import com.created.domain.model.Nickname
import com.created.domain.model.OnBoarding
import com.created.domain.repository.OnBoardingRepository
import com.created.team201.data.datasource.remote.OnBoardingDataSource

class OnBoardingRepositoryImpl(
    private val onBoardingDataSource: OnBoardingDataSource
) : OnBoardingRepository {
    override suspend fun getAvailableNickname(nickname: Nickname): Result<Boolean> {
        return runCatching {
            onBoardingDataSource.getAvailableNickname(nickname).exists
        }
    }

    override suspend fun patchOnBoarding(onBoarding: OnBoarding): Result<Unit> {
        return runCatching {
            onBoardingDataSource.patchOnBoarding(onBoarding)
        }
    }
}
