package com.created.team201.data.datasource.remote

import com.created.team201.data.remote.response.MemberResponseDto
import com.created.team201.data.remote.response.MyPageResponseDto
import com.created.team201.data.remote.response.StudyDetailResponseDto

interface StudyDetailDataSource {

    suspend fun getStudyDetail(studyId: Long): StudyDetailResponseDto

    suspend fun getStudyMemberRole(studyId: Long): Int

    suspend fun getStudyApplicants(studyId: Long): List<MemberResponseDto>

    suspend fun participateStudy(studyId: Long)

    suspend fun startStudy(studyId: Long)

    suspend fun acceptApplicant(studyId: Long, memberId: Long)

    suspend fun getMyProfile(): MyPageResponseDto
}
