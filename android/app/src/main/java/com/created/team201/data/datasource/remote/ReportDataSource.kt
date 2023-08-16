package com.created.team201.data.datasource.remote

import com.created.domain.model.ReportStudy
import com.created.domain.model.ReportUser

interface ReportDataSource {
    suspend fun reportUser(reportUser: ReportUser)

    suspend fun reportStudy(reportStudy: ReportStudy)
}
