package com.created.team201.data.datasource.remote

import com.created.domain.model.ReportStudy
import com.created.domain.model.ReportUser

interface ReportDataSource {
    suspend fun addUserReport(reportUser: ReportUser)

    suspend fun addStudyReport(reportStudy: ReportStudy)
}
