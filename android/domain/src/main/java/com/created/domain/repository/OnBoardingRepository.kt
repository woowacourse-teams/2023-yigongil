package com.created.domain.repository

import com.created.domain.model.Nickname
import com.created.domain.model.OnBoarding

interface OnBoardingRepository {
    suspend fun getAvailableNickname(nickname: Nickname): Result<Boolean>
    suspend fun patchOnBoarding(onBoarding: OnBoarding): Result<Unit>
}
