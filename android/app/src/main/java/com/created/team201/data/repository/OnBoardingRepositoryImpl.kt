package com.created.team201.data.repository

import com.created.domain.model.Nickname
import com.created.domain.model.OnBoarding
import com.created.domain.repository.OnBoardingRepository
import com.created.team201.data.datasource.local.OnBoardingIsDoneDataSource
import com.created.team201.data.datasource.remote.OnBoardingDataSource
import kotlinx.coroutines.runBlocking

class OnBoardingRepositoryImpl(
    private val onBoardingIsDoneDataSource: OnBoardingIsDoneDataSource,
    private val onBoardingDataSource: OnBoardingDataSource,
) : OnBoardingRepository {
    override suspend fun getIsOnboardingDone(): Result<Boolean> {
        return runCatching {
            var isDone: Boolean = onBoardingIsDoneDataSource.getOnBoardingIsDone()

            if (!isDone) {
                runBlocking {
                    isDone = onBoardingDataSource.getIsOnboardingDone().isOnboardingDone
                }
            }
            onBoardingIsDoneDataSource.setOnBoardingIsDone(isDone)
            isDone
        }
    }

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
