package com.created.team201.data.repository

import com.created.domain.model.CreateStudy
import com.created.domain.repository.CreateStudyRepository
import com.created.team201.data.datasource.remote.CreateStudyDataSource

class CreateStudyRepositoryImpl(
    private val createStudyDataSource: CreateStudyDataSource,
) : CreateStudyRepository {
    override suspend fun createStudy(createStudy: CreateStudy): Result<Long> {
        return runCatching {
            val location = createStudyDataSource.createStudy(createStudy)

            return@runCatching location.headers()[LOCATION_KEY]
                ?.substringAfterLast(LOCATION_DELIMITER)
                ?.toLong()
                ?: throw IllegalStateException()
        }
    }

    override suspend fun editStudy(studyId: Long, createStudy: CreateStudy) {
        createStudyDataSource.editStudy(studyId, createStudy)
    }

    companion object {
        private const val LOCATION_KEY = "Location"
        private const val LOCATION_DELIMITER = "/"
    }
}
