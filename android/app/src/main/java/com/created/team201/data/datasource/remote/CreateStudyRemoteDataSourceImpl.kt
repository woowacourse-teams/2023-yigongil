package com.created.team201.data.datasource.remote

import com.created.domain.model.CreateStudy
import com.created.team201.data.mapper.toRequestDto
import com.created.team201.data.remote.api.CreateStudyService

class CreateStudyRemoteDataSourceImpl(
    private val createStudyService: CreateStudyService,
) : CreateStudyRemoteDataSource {
    override suspend fun createStudy(createStudy: CreateStudy) {
        createStudyService.createStudy(createStudy.toRequestDto())
    }
}
