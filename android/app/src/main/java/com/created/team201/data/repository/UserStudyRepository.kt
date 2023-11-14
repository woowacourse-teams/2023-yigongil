package com.created.team201.data.repository

import com.created.team201.data.mapper.toUserStudyEntity
import com.created.team201.data.model.UserStudyEntity
import com.created.team201.data.remote.api.UserStudyService
import javax.inject.Inject

class UserStudyRepository @Inject constructor(
    private val userStudyService: UserStudyService,
) {
    suspend fun getUserStudies(): List<UserStudyEntity> =
        userStudyService.getUserStudies().map { it.toUserStudyEntity() }
}
