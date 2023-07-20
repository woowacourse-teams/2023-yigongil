package com.created.team201.data.datasource.remote

import com.created.team201.data.remote.api.StudyListService
import com.created.team201.data.remote.response.StudySummaryResponseDto

class StudyListDataSourceImpl(
    private val studyListService: StudyListService,
) : StudyListDataSource {
    override suspend fun getStudyList(): List<StudySummaryResponseDto> {
        return studyListService.getStudyList()
    }
}
