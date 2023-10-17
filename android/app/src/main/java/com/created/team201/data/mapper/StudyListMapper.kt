package com.created.team201.data.mapper

import com.created.domain.model.StudySummary
import com.created.team201.data.remote.response.StudySummaryResponseDto

fun StudySummaryResponseDto.toDomain(): StudySummary =
    StudySummary(
        id,
        processingStatus,
        averageTier,
        name,
        createdAt,
        minimumWeeks,
        meetingDaysCountPerWeek,
        numberOfCurrentMembers,
        numberOfMaximumMembers,
    )

fun List<StudySummaryResponseDto>.toDomain(): List<StudySummary> = map { it.toDomain() }
