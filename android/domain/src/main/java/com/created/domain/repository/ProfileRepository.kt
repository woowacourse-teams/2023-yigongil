package com.created.domain.repository

import com.created.domain.model.UserProfile

interface ProfileRepository {

    suspend fun getProfile(userId: Long): UserProfile
}
