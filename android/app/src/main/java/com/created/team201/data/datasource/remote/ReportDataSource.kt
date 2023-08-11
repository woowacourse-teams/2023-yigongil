package com.created.team201.data.datasource.remote

import com.created.domain.model.Report

interface ReportDataSource {
    suspend fun reportUser(report: Report)
}
