package com.created.team201.data.repository

import com.created.domain.model.UpdateStudy
import com.created.domain.repository.UpdateStudyRepository
import com.created.team201.data.datasource.remote.UpdateStudyDataSource
import javax.inject.Inject

class DefaultUpdateStudyRepository @Inject constructor(
    private val updateStudyDataSource: UpdateStudyDataSource,
) : UpdateStudyRepository {
    override suspend fun createStudy(updateStudy: UpdateStudy): Result<Long> {
        return runCatching {
            val location = updateStudyDataSource.createStudy(updateStudy)

            return@runCatching location.headers()[LOCATION_KEY]
                ?.substringAfterLast(LOCATION_DELIMITER)
                ?.toLong()
                ?: throw IllegalStateException()
        }
    }

    override suspend fun editStudy(studyId: Long, updateStudy: UpdateStudy) {
        updateStudyDataSource.editStudy(studyId, updateStudy)
    }

    companion object {
        private const val LOCATION_KEY = "Location"
        private const val LOCATION_DELIMITER = "/"
    }
}
