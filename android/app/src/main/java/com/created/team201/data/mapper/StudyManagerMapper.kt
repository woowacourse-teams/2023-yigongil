package com.created.team201.data.mapper

import com.created.domain.Period
import com.created.domain.PeriodUnit
import com.created.domain.model.Role
import com.created.domain.model.StudyManage
import com.created.team201.data.remote.response.StudyManageResponseDto

fun StudyManageResponseDto.toDomain(): StudyManage =
    StudyManage(
        id,
        processingStatus,
        tier,
        Role.valueOf(role),
        title,
        date,
        totalRound,
        period.toPeriod(),
        currentMember,
        maximumMember,
    )

fun List<StudyManageResponseDto>.toDomain(): List<StudyManage> =
    this.map { it.toDomain() }

private fun String.toPeriod(): Period =
    Period(Character.getNumericValue(this.first()), PeriodUnit.valueOf(this.last()))
