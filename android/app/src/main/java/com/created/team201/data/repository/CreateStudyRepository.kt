package com.created.team201.data.repository

import com.created.domain.model.CreateStudy
import com.created.team201.data.mapper.toRequestDto
import com.created.team201.data.remote.api.CreateStudyService
import javax.inject.Inject

class CreateStudyRepository @Inject constructor(
    private val createStudyService: CreateStudyService,
) {

    suspend fun createStudy(createStudy: CreateStudy): Result<Long> {
        return runCatching {
            val location = createStudyService.createStudy(createStudy.toRequestDto())

            return@runCatching location.headers()[LOCATION_KEY]
                ?.substringAfterLast(LOCATION_DELIMITER)
                ?.toLong()
                ?: throw IllegalStateException()
        }
    }

    companion object {
        private const val LOCATION_KEY = "Location"
        private const val LOCATION_DELIMITER = "/"
    }
}
