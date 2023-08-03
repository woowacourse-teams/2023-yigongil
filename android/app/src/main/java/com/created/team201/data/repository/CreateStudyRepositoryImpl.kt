package com.created.team201.data.repository

import com.created.domain.model.CreateStudy
import com.created.domain.repository.CreateStudyRepository
import com.created.team201.data.datasource.remote.CreateStudyDataSource

class CreateStudyRepositoryImpl(
    private val createStudyDataSource: CreateStudyDataSource,
) : CreateStudyRepository {
    override suspend fun createStudy(createStudy: CreateStudy): Result<Unit> {
        return runCatching {
            createStudyDataSource.createStudy(createStudy)
        }
    }
}
