package com.created.team201.data.datasource.remote

import com.created.team201.data.remote.response.StudyDetailResponseDto

interface StudyDetailDataSource {

    suspend fun getStudyDetail(studyId: Long): StudyDetailResponseDto
}
