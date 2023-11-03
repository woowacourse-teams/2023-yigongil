package com.created.team201.data.repository

import com.created.domain.model.UserStudy
import com.created.team201.data.mapper.toUserStudy
import com.created.team201.data.remote.api.UserStudyService
import javax.inject.Inject

class UserStudyRepository @Inject constructor(
    private val userStudyService: UserStudyService,
) {
    suspend fun getUserStudies(): List<UserStudy> =
        userStudyService.getUserStudies().map { it.toUserStudy() }
}
