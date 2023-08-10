package com.created.team201.data.datasource.remote

import com.created.domain.model.UpdateStudy
import com.created.team201.data.mapper.toRequestDto
import com.created.team201.data.remote.api.UpdateStudyService
import retrofit2.Response

class UpdateStudyDataSourceImpl(
    private val updateStudyService: UpdateStudyService,
) : UpdateStudyDataSource {
    override suspend fun createStudy(updateStudy: UpdateStudy): Response<Unit> {
        return updateStudyService.createStudy(updateStudy.toRequestDto())
    }

    override suspend fun editStudy(studyId: Long, updateStudy: UpdateStudy) {
        updateStudyService.editStudy(studyId, updateStudy.toRequestDto())
    }
}
