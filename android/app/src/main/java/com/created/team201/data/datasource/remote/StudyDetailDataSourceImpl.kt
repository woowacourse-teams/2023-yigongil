package com.created.team201.data.datasource.remote

import com.created.team201.data.remote.api.StudyDetailService
import com.created.team201.data.remote.response.MemberResponseDto
import com.created.team201.data.remote.response.MyPageResponseDto
import com.created.team201.data.remote.response.StudyDetailResponseDto

class StudyDetailDataSourceImpl(
    private val studyDetailService: StudyDetailService,
) : StudyDetailDataSource {
    override suspend fun getStudyDetail(studyId: Long): StudyDetailResponseDto {
        return studyDetailService.getStudyDetail(studyId)
    }

    override suspend fun getStudyApplicants(studyId: Long): List<MemberResponseDto> {
        return studyDetailService.getStudyApplicants(studyId)
    }

    override suspend fun participateStudy(studyId: Long) {
        studyDetailService.participateStudy(studyId)
    }

    override suspend fun startStudy(studyId: Long) {
        studyDetailService.startStudy(studyId)
    }

    override suspend fun acceptApplicant(studyId: Long, memberId: Long) {
        studyDetailService.acceptApplicant(studyId, memberId)
    }

    override suspend fun getMyProfile(): MyPageResponseDto {
        return studyDetailService.getMyProfile()
    }
}
