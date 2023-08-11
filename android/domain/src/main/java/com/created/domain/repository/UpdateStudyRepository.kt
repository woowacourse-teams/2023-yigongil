package com.created.domain.repository

import com.created.domain.model.UpdateStudy

interface UpdateStudyRepository {
    suspend fun createStudy(updateStudy: UpdateStudy): Result<Long>

    suspend fun editStudy(studyId: Long, updateStudy: UpdateStudy)
}
