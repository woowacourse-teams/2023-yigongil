package com.created.domain.repository

import com.created.domain.model.Role
import com.created.domain.model.StudySummary

interface StudyListRepository {

    suspend fun getStudyList(status: String?, page: Int?, searchWord: String?): List<StudySummary>

    suspend fun getAppliedStudyList(searchWord: String?, role: Role): List<StudySummary>
}
