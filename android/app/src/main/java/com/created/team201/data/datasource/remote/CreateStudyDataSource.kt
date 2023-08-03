package com.created.team201.data.datasource.remote

import com.created.domain.model.CreateStudy
import retrofit2.Response

interface CreateStudyDataSource {
    suspend fun createStudy(createStudy: CreateStudy): Response<Unit>
}
