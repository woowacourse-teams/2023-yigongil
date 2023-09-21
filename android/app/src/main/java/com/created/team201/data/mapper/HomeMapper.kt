package com.created.team201.data.mapper

import com.created.domain.model.UserStudy
import com.created.team201.data.remote.response.HomeStudyResponseDto


fun HomeStudyResponseDto.toUserStudy(): UserStudy =
    UserStudy(
        isMaster = isMaster,
        userId = userId,
        name = name,
        mustDo = mustDo,
        leftDays = leftDays,
        grassSeedsCount = grassSeedsCount,
    )