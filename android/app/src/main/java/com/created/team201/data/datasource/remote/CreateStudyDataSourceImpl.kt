package com.created.team201.data.datasource.remote

import com.created.domain.model.CreateStudy
import com.created.team201.data.mapper.toRequestDto
import com.created.team201.data.remote.api.CreateStudyService
import retrofit2.Response

class CreateStudyDataSourceImpl(
    private val createStudyService: CreateStudyService,
) : CreateStudyDataSource {
    override suspend fun createStudy(createStudy: CreateStudy): Response<Unit> {
        return createStudyService.createStudy(createStudy.toRequestDto())
    }
}
