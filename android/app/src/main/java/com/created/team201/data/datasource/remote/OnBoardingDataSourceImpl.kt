package com.created.team201.data.datasource.remote

import com.created.domain.model.Nickname
import com.created.domain.model.OnBoarding
import com.created.team201.data.mapper.toRequestDto
import com.created.team201.data.remote.api.OnBoardingService
import com.created.team201.data.remote.response.NicknameResponseDto

class OnBoardingDataSourceImpl(
    private val onBoardingService: OnBoardingService
) : OnBoardingDataSource {
    override suspend fun getAvailableNickname(nickname: Nickname): NicknameResponseDto {
        return onBoardingService.getAvailableNickname(nickname.nickname)
    }

    override suspend fun patchOnBoarding(onBoarding: OnBoarding) {
        return onBoardingService.patchOnBoarding(onBoarding.toRequestDto())
    }
}
