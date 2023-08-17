package com.created.domain.repository

import com.created.domain.model.ReportStudy
import com.created.domain.model.ReportUser

interface ReportRepository {
    suspend fun reportUser(reportUser: ReportUser)

    suspend fun reportStudy(reportStudy: ReportStudy)
}
