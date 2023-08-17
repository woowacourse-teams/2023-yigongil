package com.created.team201.data.datasource.remote

import com.created.domain.model.ReportStudy
import com.created.domain.model.ReportUser
import com.created.team201.data.mapper.toRequestDto
import com.created.team201.data.remote.api.ReportService

class ReportDataSourceImpl(
    private val reportService: ReportService,
) : ReportDataSource {
    override suspend fun addUserReport(reportUser: ReportUser) {
        reportService.reportUser(reportUser.toRequestDto())
    }

    override suspend fun addStudyReport(reportStudy: ReportStudy) {
        reportService.reportStudy(reportStudy.toRequestDto())
    }
}
