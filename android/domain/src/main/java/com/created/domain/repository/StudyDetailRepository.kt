package com.created.domain.repository

import com.created.domain.model.Member
import com.created.domain.model.Profile
import com.created.domain.model.StudyDetail

interface StudyDetailRepository {

    suspend fun getStudyDetail(studyId: Long): StudyDetail

    suspend fun getStudyMemberRole(studyId: Long): Int

    suspend fun getStudyApplicants(studyId: Long): List<Member>

    suspend fun participateStudy(studyId: Long)

    suspend fun startStudy(studyId: Long)

    suspend fun acceptApplicant(studyId: Long, memberId: Long)

    suspend fun getMyProfile(): Profile
}
