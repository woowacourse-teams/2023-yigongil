package com.created.team201.data.mapper

import com.created.domain.model.OnBoarding
import com.created.team201.data.remote.request.OnBoardingRequestDto

fun OnBoarding.toRequestDto(): OnBoardingRequestDto = OnBoardingRequestDto(
    nickname.nickname,
    introduction,
)
