package com.created.team201.data.remote.api

import com.created.team201.data.remote.request.OnBoardingRequestDto
import com.created.team201.data.remote.response.OnBoardingResponseDto
import retrofit2.http.Body
import retrofit2.http.PATCH

interface OnBoardingService {
    @PATCH("/v1/members")
    suspend fun patchOnBoarding(@Body onBoardingRequestDto: OnBoardingRequestDto): OnBoardingResponseDto
}
