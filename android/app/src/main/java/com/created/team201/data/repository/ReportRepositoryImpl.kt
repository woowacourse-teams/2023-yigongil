package com.created.team201.data.repository

import com.created.domain.model.Report
import com.created.domain.repository.ReportRepository
import com.created.team201.data.datasource.remote.ReportDataSource

class ReportRepositoryImpl(
    private val reportDataSource: ReportDataSource,
) : ReportRepository {
    override suspend fun reportTarget(report: Report) {
        reportDataSource.reportTarget(report)
    }
}
