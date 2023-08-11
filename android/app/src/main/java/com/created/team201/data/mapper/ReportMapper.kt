package com.created.team201.data.mapper

import com.created.domain.model.Report
import com.created.team201.data.remote.request.ReportRequestDTO

fun Report.toRequestDTO(): ReportRequestDTO = ReportRequestDTO(
    reportedMemberId,
    title,
    problemOccuredAt,
    content,
)
