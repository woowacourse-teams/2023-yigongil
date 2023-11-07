package com.created.team201.data.mapper

import com.created.team201.data.model.UserStudyEntity
import com.created.team201.data.remote.response.UserStudyResponseDto

fun UserStudyResponseDto.toUserStudyEntity(): UserStudyEntity =
    UserStudyEntity(
        isMaster = isMaster,
        studyId = studyId,
        studyName = studyName,
        mustDo = mustDo ?: "",
        leftDays = leftDays,
        grassSeedsCount = grassSeedsCount,
    )
