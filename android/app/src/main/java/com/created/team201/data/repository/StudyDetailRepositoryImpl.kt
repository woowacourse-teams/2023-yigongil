package com.created.team201.data.repository

import com.created.domain.model.StudyDetail
import com.created.domain.repository.StudyDetailRepository
import com.created.team201.data.datasource.remote.StudyDetailDataSource
import com.created.team201.data.mapper.toDomain

class StudyDetailRepositoryImpl(
    private val studyDetailDataSource: StudyDetailDataSource,
) : StudyDetailRepository {
    override suspend fun getStudyDetail(studyId: Long): StudyDetail {
        return studyDetailDataSource.getStudyDetail(studyId).toDomain()
    }
}
