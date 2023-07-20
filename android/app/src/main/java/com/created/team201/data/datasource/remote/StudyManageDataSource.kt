package com.created.team201.data.datasource.remote

import com.created.team201.data.remote.response.StudyManageResponseDto

interface StudyManageDataSource {

    suspend fun getMyStudies(): List<StudyManageResponseDto>
}
