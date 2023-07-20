package com.created.team201.data.datasource.remote

import com.created.team201.data.remote.api.StudyManageService
import com.created.team201.data.remote.response.StudyManageResponseDto

class StudyManageDataSourceImpl(
    private val studyManageService: StudyManageService,
) : StudyManageDataSource {

    override suspend fun getMyStudies(): List<StudyManageResponseDto> =
        studyManageService.getMyStudies()
}
