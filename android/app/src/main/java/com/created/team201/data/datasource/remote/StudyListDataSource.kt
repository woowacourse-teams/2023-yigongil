package com.created.team201.data.datasource.remote

import com.created.team201.data.remote.response.StudySummaryResponseDto

interface StudyListDataSource {

    suspend fun getStudyList(page: Int): List<StudySummaryResponseDto>

    suspend fun getSearchedStudyList(searchWord: String, page: Int): List<StudySummaryResponseDto>
}
