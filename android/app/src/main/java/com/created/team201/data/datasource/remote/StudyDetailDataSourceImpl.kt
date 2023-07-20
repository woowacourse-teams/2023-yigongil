package com.created.team201.data.datasource.remote

import com.created.team201.data.remote.api.StudyDetailService
import com.created.team201.data.remote.response.StudyDetailResponseDto

class StudyDetailDataSourceImpl(
    private val studyDetailService: StudyDetailService,
) : StudyDetailDataSource{
    override suspend fun getStudyDetail(studyId: Long): StudyDetailResponseDto {
        return studyDetailService.getStudyDetail(studyId)
    }
}
