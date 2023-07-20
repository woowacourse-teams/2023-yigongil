package com.created.domain.repository

import com.created.domain.model.StudyDetail

interface StudyDetailRepository {

    suspend fun getStudyDetail(studyId: Long): StudyDetail
}
