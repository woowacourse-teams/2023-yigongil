package com.created.team201.data.repository

import com.created.domain.model.CreateStudy
import com.created.domain.repository.CreateStudyRepository
import com.created.team201.data.datasource.remote.CreateStudyRemoteDataSource

class CreateStudyRepositoryImpl(
    private val createStudyRemoteDataSource: CreateStudyRemoteDataSource,
) : CreateStudyRepository {
    override suspend fun createStudy(createStudy: CreateStudy) {
        createStudyRemoteDataSource.createStudy(createStudy)
    }
}
