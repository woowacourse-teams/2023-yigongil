package com.created.team201.data.repository

import com.created.domain.model.UserProfile
import com.created.team201.data.mapper.toDomain
import com.created.team201.data.remote.api.ProfileService
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileService: ProfileService,
) {

    suspend fun getProfile(userId: Long): UserProfile {
        return profileService.getProfile(userId).toDomain()
    }
}
