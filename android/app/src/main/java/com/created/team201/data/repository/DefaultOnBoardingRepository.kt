package com.created.team201.data.repository

import com.created.domain.model.Nickname
import com.created.domain.model.OnBoarding
import com.created.domain.repository.OnBoardingRepository
import com.created.team201.data.datasource.local.OnBoardingDataSource
import com.created.team201.data.mapper.toRequestDto
import com.created.team201.data.remote.api.OnBoardingService
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class DefaultOnBoardingRepository @Inject constructor(
    private val onBoardingDataSource: OnBoardingDataSource,
    private val onBoardingService: OnBoardingService,
) : OnBoardingRepository {
    override suspend fun getIsOnboardingDone(): Result<Boolean> {
        return runCatching {
            var isDone: Boolean = onBoardingDataSource.getOnBoardingIsDone()

            if (!isDone) {
                runBlocking {
                    isDone = onBoardingService.getIsOnboardingDone().isOnboardingDone
                }
            }
            onBoardingDataSource.setOnBoardingIsDone(isDone)
            isDone
        }
    }

    override suspend fun getAvailableNickname(nickname: Nickname): Result<Boolean> {
        return runCatching {
            onBoardingService.getAvailableNickname(nickname.nickname).exists
        }
    }

    override suspend fun patchOnBoarding(onBoarding: OnBoarding): Result<Unit> {
        return runCatching {
            onBoardingService.patchOnBoarding(onBoarding.toRequestDto())
        }
    }
}
