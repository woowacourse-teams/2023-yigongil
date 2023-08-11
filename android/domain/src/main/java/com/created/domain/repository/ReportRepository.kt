package com.created.domain.repository

import com.created.domain.model.Report

interface ReportRepository {
    suspend fun reportUser(report: Report)
}
