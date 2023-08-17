package com.created.team201.data.mapper

import com.created.domain.model.ReportStudy
import com.created.domain.model.ReportUser
import com.created.team201.data.remote.request.ReportStudyRequestDto
import com.created.team201.data.remote.request.ReportUserRequestDto

fun ReportUser.toRequestDto(): ReportUserRequestDto = ReportUserRequestDto(
    reportedMemberId,
    title,
    problemOccuredAt,
    content,
)

fun ReportStudy.toRequestDto(): ReportStudyRequestDto = ReportStudyRequestDto(
    reportedStudyId,
    title,
    problemOccurredAt,
    content,
)
