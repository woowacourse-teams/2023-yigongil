package com.created.domain.repository

import com.created.domain.model.OnBoarding

interface OnBoardingRepository {
    suspend fun patchOnBoarding(onBoarding: OnBoarding): Result<String>
}
