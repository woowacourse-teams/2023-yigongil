package com.created.team201.data.datasource.remote

import com.created.domain.model.OnBoarding
import com.created.team201.data.remote.response.OnBoardingResponseDto

interface OnBoardingDataSource {
    suspend fun patchOnBoarding(onBoarding: OnBoarding): OnBoardingResponseDto
}
