package com.created.team201.data.repository

import com.created.domain.model.UpdateStudy
import com.created.domain.repository.UpdateStudyRepository
import com.created.team201.data.mapper.toRequestDto
import com.created.team201.data.remote.api.UpdateStudyService
import javax.inject.Inject

class DefaultUpdateStudyRepository @Inject constructor(
    private val updateStudyService: UpdateStudyService
) : UpdateStudyRepository {
    override suspend fun createStudy(updateStudy: UpdateStudy): Result<Long> {
        return runCatching {
            val location = updateStudyService.createStudy(updateStudy.toRequestDto())

            return@runCatching location.headers()[LOCATION_KEY]
                ?.substringAfterLast(LOCATION_DELIMITER)
                ?.toLong()
                ?: throw IllegalStateException()
        }
    }

    override suspend fun editStudy(studyId: Long, updateStudy: UpdateStudy) {
        updateStudyService.editStudy(studyId, updateStudy.toRequestDto())
    }

    companion object {
        private const val LOCATION_KEY = "Location"
        private const val LOCATION_DELIMITER = "/"
    }
}
