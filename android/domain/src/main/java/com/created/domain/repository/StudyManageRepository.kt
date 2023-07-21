package com.created.domain.repository

import com.created.domain.model.StudyManage

interface StudyManageRepository {

    suspend fun getMyStudies(): List<StudyManage>
}
