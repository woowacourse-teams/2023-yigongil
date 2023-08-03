package com.created.team201.data.datasource.remote

import com.created.team201.data.remote.api.ProfileService
import com.created.team201.data.remote.response.ProfileResponseDto

class ProfileDataSourceImpl(
    private val profileService: ProfileService,
): ProfileDataSource {
    override suspend fun getProfile(userId: Long): ProfileResponseDto {
        return profileService.getProfile(userId)
    }
}
