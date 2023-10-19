package com.created.team201.data.repository

import com.created.domain.model.Role
import com.created.domain.model.StudySummary
import com.created.domain.repository.StudyListRepository
import com.created.team201.data.mapper.toDomain
import com.created.team201.data.remote.api.CommonStudyListService
import com.created.team201.data.remote.api.MemberStudyListService
import javax.inject.Inject

class DefaultStudyListRepository @Inject constructor(
    private val commonStudyListService: CommonStudyListService,
    private val memberStudyListService: MemberStudyListService,
) : StudyListRepository {

    override suspend fun getStudyList(
        status: String?,
        page: Int?,
        searchWord: String?,
    ): List<StudySummary> {
        return commonStudyListService.getStudyList(status, page, searchWord).toDomain()
    }

    override suspend fun getAppliedStudyList(searchWord: String?, role: Role): List<StudySummary> {
        return memberStudyListService.getAppliedStudyList(searchWord, role.name).toDomain()
    }

    override suspend fun getCreatedStudyList(): List<StudySummary> {
        return memberStudyListService.getCreatedStudyList().toDomain()
    }
}
