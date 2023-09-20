package com.created.team201.data.repository

import com.created.domain.model.Member
import com.created.domain.model.Profile
import com.created.domain.model.StudyDetail
import com.created.domain.repository.StudyDetailRepository
import com.created.team201.data.mapper.toDomain
import com.created.team201.data.remote.api.StudyDetailService
import javax.inject.Inject

class DefaultStudyDetailRepository @Inject constructor(
    private val studyDetailService: StudyDetailService,
) : StudyDetailRepository {
    override suspend fun getStudyDetail(studyId: Long): StudyDetail {
        return studyDetailService.getStudyDetail(studyId).toDomain()
    }

    override suspend fun participateStudy(studyId: Long) {
        studyDetailService.participateStudy(studyId)
    }

    override suspend fun startStudy(studyId: Long) {
        studyDetailService.startStudy(studyId)
    }

    override suspend fun getStudyApplicants(studyId: Long): List<Member> {
        return studyDetailService.getStudyApplicants(studyId).map { it.toDomain() }
    }

    override suspend fun acceptApplicant(studyId: Long, memberId: Long) {
        studyDetailService.acceptApplicant(studyId, memberId)
    }

    override suspend fun getMyProfile(): Profile {
        return studyDetailService.getMyProfile().toDomain()
    }
}
