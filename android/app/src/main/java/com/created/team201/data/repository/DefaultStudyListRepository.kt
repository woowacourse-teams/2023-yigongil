package com.created.team201.data.repository

import com.created.domain.model.StudySummary
import com.created.domain.repository.StudyListRepository
import com.created.team201.data.mapper.toDomain
import com.created.team201.data.remote.api.StudyListService
import javax.inject.Inject

class DefaultStudyListRepository @Inject constructor(
    private val studyListService: StudyListService
) : StudyListRepository {

    override suspend fun getStudyList(page: Int): List<StudySummary> {
        return studyListService.getStudyList(page).toDomain()
    }

    override suspend fun getSearchedStudyList(searchWord: String, page: Int): List<StudySummary> {
        return studyListService.getSearchedStudyList(searchWord, page).toDomain()
    }
}
