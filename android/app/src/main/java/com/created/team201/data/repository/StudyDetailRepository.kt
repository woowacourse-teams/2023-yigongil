package com.created.team201.data.repository

import com.created.domain.model.Member
import com.created.domain.model.Profile
import com.created.domain.model.StudyDetail
import com.created.domain.model.StudyStart
import com.created.team201.data.mapper.toDomain
import com.created.team201.data.mapper.toRequestDto
import com.created.team201.data.remote.api.StudyDetailService
import javax.inject.Inject

class StudyDetailRepository @Inject constructor(
    private val studyDetailService: StudyDetailService,
) {

    suspend fun getStudyDetail(studyId: Long): StudyDetail {
        return studyDetailService.getStudyDetail(studyId).toDomain()
    }

    suspend fun getStudyMemberRole(studyId: Long): Result<Int> {
        return runCatching {
            studyDetailService.getStudyMemberRole(studyId).role
        }
    }

    suspend fun participateStudy(studyId: Long) {
        studyDetailService.participateStudy(studyId)
    }

    suspend fun startStudy(studyId: Long, studyStart: StudyStart) {
        studyDetailService.startStudy(studyId, studyStart.toRequestDto())
    }

    suspend fun getStudyApplicants(studyId: Long): List<Member> {
        return studyDetailService.getStudyApplicants(studyId).map { it.toDomain() }
    }

    suspend fun acceptApplicant(studyId: Long, memberId: Long) {
        studyDetailService.acceptApplicant(studyId, memberId)
    }

    suspend fun getMyProfile(): Profile {
        return studyDetailService.getMyProfile().toDomain()
    }
}
