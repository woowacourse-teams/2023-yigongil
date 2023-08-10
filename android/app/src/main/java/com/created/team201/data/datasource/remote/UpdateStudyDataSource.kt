package com.created.team201.data.datasource.remote

import com.created.domain.model.UpdateStudy
import retrofit2.Response

interface UpdateStudyDataSource {
    suspend fun createStudy(updateStudy: UpdateStudy): Response<Unit>

    suspend fun editStudy(studyId: Long, updateStudy: UpdateStudy)
}
