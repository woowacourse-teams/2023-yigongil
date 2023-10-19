package com.created.team201.data.mapper

import com.created.domain.model.MustDoStatus
import com.created.domain.model.WeeklyMustDo
import com.created.team201.data.remote.response.WeeklyMustDoResponseDto

fun WeeklyMustDoResponseDto.toDomain(): WeeklyMustDo = WeeklyMustDo(
    dayOfWeek = dayOfWeek,
    id = id,
    mustDo = mustDo ?: "",
    status = MustDoStatus.of(status),
)
