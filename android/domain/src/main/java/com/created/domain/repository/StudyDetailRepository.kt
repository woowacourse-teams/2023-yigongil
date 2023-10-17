package com.created.domain.repository

import com.created.domain.model.Member
import com.created.domain.model.Profile
import com.created.domain.model.StudyDetail
import com.created.domain.model.StudyStart

interface StudyDetailRepository {

    suspend fun getStudyDetail(studyId: Long): StudyDetail

    suspend fun getStudyMemberRole(studyId: Long): Result<Int>

    suspend fun getStudyApplicants(studyId: Long): List<Member>

    suspend fun participateStudy(studyId: Long)

    suspend fun startStudy(studyId: Long, studyStart: StudyStart)

    suspend fun acceptApplicant(studyId: Long, memberId: Long)

    suspend fun getMyProfile(): Profile
}
