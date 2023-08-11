package com.created.team201.data.datasource.remote

import com.created.domain.model.Report
import com.created.team201.data.mapper.toRequestDTO
import com.created.team201.data.remote.api.ReportService

class ReportDataSourceImpl(
    private val reportService: ReportService,
) : ReportDataSource {
    override suspend fun reportUser(report: Report) {
        reportService.reportUser(report.toRequestDTO())
    }
}
