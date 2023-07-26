package com.created.team201.data.mapper

import com.created.domain.Period
import com.created.domain.PeriodUnit
import com.created.domain.StudySummary
import com.created.domain.model.Role
import com.created.domain.model.StudyManage
import com.created.team201.data.remote.response.StudyManageResponseDto

fun StudyManageResponseDto.toDomain(): StudyManage =
    StudyManage(
        StudySummary(
            id,
            processingStatus,
            tier,
            title,
            date,
            totalRound,
            period.toPeriod(),
            currentMember,
            maximumMember,
        ),
        Role.valueOf(role),
    )

fun List<StudyManageResponseDto>.toDomain(): List<StudyManage> = map { it.toDomain() }

private fun String.toPeriod(): Period =
    Period(Character.getNumericValue(first()), PeriodUnit.valueOf(last()))
