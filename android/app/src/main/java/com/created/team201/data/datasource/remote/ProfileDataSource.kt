package com.created.team201.data.datasource.remote

import com.created.team201.data.remote.response.ProfileResponseDto

interface ProfileDataSource {

    suspend fun getProfile(userId: Long): ProfileResponseDto
}
