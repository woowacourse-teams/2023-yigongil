package com.created.team201.data.mapper

import com.created.domain.model.ReportUser
import com.created.team201.data.remote.request.ReportUserRequestDto

fun ReportUser.toRequestDTO(): ReportUserRequestDto = ReportUserRequestDto(
    reportedMemberId,
    title,
    problemOccuredAt,
    content,
)
