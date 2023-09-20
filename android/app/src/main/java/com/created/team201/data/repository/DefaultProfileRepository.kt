package com.created.team201.data.repository

import com.created.domain.model.UserProfile
import com.created.domain.repository.ProfileRepository
import com.created.team201.data.datasource.remote.ProfileDataSource
import com.created.team201.data.mapper.toDomain

class DefaultProfileRepository(
    private val profileDataSource: ProfileDataSource,
) : ProfileRepository {
    override suspend fun getProfile(userId: Long): UserProfile {
        return profileDataSource.getProfile(userId).toDomain()
    }
}
