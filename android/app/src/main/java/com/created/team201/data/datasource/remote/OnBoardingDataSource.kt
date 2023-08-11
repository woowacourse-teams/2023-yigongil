package com.created.team201.data.datasource.remote

import com.created.domain.model.Nickname
import com.created.domain.model.OnBoarding
import com.created.team201.data.remote.response.IsOnBoardingDoneResponseDto
import com.created.team201.data.remote.response.NicknameResponseDto

interface OnBoardingDataSource {
    suspend fun getIsOnboardingDone(): IsOnBoardingDoneResponseDto
    suspend fun getAvailableNickname(nickname: Nickname): NicknameResponseDto
    suspend fun patchOnBoarding(onBoarding: OnBoarding)
}
