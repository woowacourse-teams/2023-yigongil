package com.created.team201.data.mapper

import com.created.domain.model.UserStudy
import com.created.team201.data.remote.response.UserStudyResponseDto


fun UserStudyResponseDto.toUserStudy(): UserStudy =
    UserStudy(
        isMaster = isMaster,
        studyId = studyId,
        studyName = studyName,
        mustDo = mustDo ?: "",
        leftDays = leftDays,
        grassSeedsCount = grassSeedsCount,
    )