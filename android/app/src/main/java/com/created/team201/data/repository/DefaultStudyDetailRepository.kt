package com.created.team201.data.repository

import com.created.domain.model.Member
import com.created.domain.model.Profile
import com.created.domain.model.StudyDetail
import com.created.domain.repository.StudyDetailRepository
import com.created.team201.data.datasource.remote.StudyDetailDataSource
import com.created.team201.data.mapper.toDomain
import javax.inject.Inject

class DefaultStudyDetailRepository @Inject constructor(
    private val studyDetailDataSource: StudyDetailDataSource,
) : StudyDetailRepository {
    override suspend fun getStudyDetail(studyId: Long): StudyDetail {
        return studyDetailDataSource.getStudyDetail(studyId).toDomain()
    }

    override suspend fun participateStudy(studyId: Long) {
        studyDetailDataSource.participateStudy(studyId)
    }

    override suspend fun startStudy(studyId: Long) {
        studyDetailDataSource.startStudy(studyId)
    }

    override suspend fun getStudyApplicants(studyId: Long): List<Member> {
        return studyDetailDataSource.getStudyApplicants(studyId).map { it.toDomain() }
    }

    override suspend fun acceptApplicant(studyId: Long, memberId: Long) {
        studyDetailDataSource.acceptApplicant(studyId, memberId)
    }

    override suspend fun getMyProfile(): Profile {
        return studyDetailDataSource.getMyProfile().toDomain()
    }
}
