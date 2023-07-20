package com.created.team201.data.mapper

import com.created.domain.StudySummary
import com.created.team201.data.remote.response.StudySummaryResponseDto

fun StudySummaryResponseDto.toDomain(): StudySummary =
    StudySummary(
        id = id,
        processingStatus,
        tier,
        title,
        date,
        totalRound,
        period,
        currentMember,
        maximumMember,
    )

fun List<StudySummaryResponseDto>.toDomain(): List<StudySummary> =
    this.map { it.toDomain() }
