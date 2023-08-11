package com.created.team201.data.remote.api

import com.created.team201.data.remote.request.ReportRequestDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportService {
    @POST("/v1/reports")
    suspend fun reportUser(
        @Body report: ReportRequestDTO,
    )
}
