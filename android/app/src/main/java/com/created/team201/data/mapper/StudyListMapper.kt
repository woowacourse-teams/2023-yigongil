package com.created.team201.data.mapper

import com.created.domain.Period
import com.created.domain.PeriodUnit
import com.created.domain.StudySummary
import com.created.team201.data.remote.response.StudySummaryResponseDto
import java.lang.Character.getNumericValue

fun StudySummaryResponseDto.toDomain(): StudySummary =
    StudySummary(
        id = id,
        processingStatus,
        tier,
        title,
        date,
        totalRound,
        period.toPeriod(),
        currentMember,
        maximumMember,
    )

fun List<StudySummaryResponseDto>.toDomain(): List<StudySummary> =
    this.map { it.toDomain() }

private fun String.toPeriod(): Period =
    Period(getNumericValue(this.first()), PeriodUnit.valueOf(this.last()))
