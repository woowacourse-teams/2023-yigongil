package com.created.team201.data.repository

import com.created.domain.model.ReportStudy
import com.created.domain.model.ReportUser
import com.created.domain.repository.ReportRepository
import com.created.team201.data.mapper.toRequestDto
import com.created.team201.data.remote.api.ReportService
import javax.inject.Inject

class DefaultReportRepository @Inject constructor(
    private val reportService: ReportService,
) : ReportRepository {
    override suspend fun reportUser(reportUser: ReportUser) {
        reportService.reportUser(reportUser.toRequestDto())
    }

    override suspend fun reportStudy(reportStudy: ReportStudy) {
        reportService.reportStudy(reportStudy.toRequestDto())
    }
}
