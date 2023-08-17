package com.created.team201.data.repository

import com.created.domain.model.ReportStudy
import com.created.domain.model.ReportUser
import com.created.domain.repository.ReportRepository
import com.created.team201.data.datasource.remote.ReportDataSource

class ReportRepositoryImpl(
    private val reportDataSource: ReportDataSource,
) : ReportRepository {
    override suspend fun reportUser(reportUser: ReportUser) {
        reportDataSource.addUserReport(reportUser)
    }

    override suspend fun reportStudy(reportStudy: ReportStudy) {
        reportDataSource.addStudyReport(reportStudy)
    }
}
