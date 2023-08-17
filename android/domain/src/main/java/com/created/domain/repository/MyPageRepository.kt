package com.created.domain.repository

import com.created.domain.model.ProfileInformation
import com.created.domain.model.Nickname
import com.created.domain.model.Profile

interface MyPageRepository {

    suspend fun getMyPage(): Result<Profile>
    suspend fun getAvailableNickname(nickname: Nickname): Result<Boolean>
    suspend fun patchMyProfile(profileInformation: ProfileInformation): Result<Unit>
}
