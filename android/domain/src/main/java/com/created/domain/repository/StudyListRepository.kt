package com.created.domain.repository

import com.created.domain.StudySummary

interface StudyListRepository {

    suspend fun getStudyList(page: Int): List<StudySummary>
}
