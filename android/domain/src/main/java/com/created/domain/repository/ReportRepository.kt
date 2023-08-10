package com.created.domain.repository

import com.created.domain.model.Report

interface ReportRepository {
    suspend fun reportTarget(report: Report)
}
