package com.created.team201.data.mapper

import com.created.domain.model.UpdateStudy
import com.created.team201.data.remote.request.UpdateStudyRequestDto

fun UpdateStudy.toRequestDto(): UpdateStudyRequestDto =
    UpdateStudyRequestDto(
        name,
        peopleCount,
        startDate,
        period,
        UpdateStudyRequestDto.getPeriodString(cycle),
        introduction,
    )
