package com.created.team201.data.repository

import com.created.domain.model.StudyManage
import com.created.domain.repository.StudyManageRepository
import com.created.team201.data.datasource.remote.StudyManageDataSource
import com.created.team201.data.mapper.toDomain

class StudyManageRepositoryImpl(
    private val studyManageDataSource: StudyManageDataSource,
) : StudyManageRepository {

    override suspend fun getMyStudies(): List<StudyManage> =
        studyManageDataSource.getMyStudies().toDomain()
}
