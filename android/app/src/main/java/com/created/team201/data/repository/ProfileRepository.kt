package com.created.team201.data.repository

import com.created.team201.data.mapper.toEntity
import com.created.team201.data.model.UserProfileEntity
import com.created.team201.data.remote.api.ProfileService
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileService: ProfileService,
) {

    suspend fun getProfile(userId: Long): UserProfileEntity {
        return profileService.getProfile(userId).toEntity()
    }
}
