package com.created.team201.data.datasource.remote

import com.created.domain.model.OnBoarding
import com.created.team201.data.mapper.toRequestDto
import com.created.team201.data.remote.api.OnBoardingService
import com.created.team201.data.remote.response.OnBoardingResponseDto

class OnBoardingDataSourceImpl(
    private val onBoardingService: OnBoardingService
) : OnBoardingDataSource {
    override suspend fun patchOnBoarding(onBoarding: OnBoarding): OnBoardingResponseDto {
        return onBoardingService.patchOnBoarding(onBoarding.toRequestDto())
    }
}
