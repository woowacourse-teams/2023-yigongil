package com.created.team201.data.repository

import com.created.domain.model.Nickname
import com.created.domain.model.Profile
import com.created.domain.model.ProfileInformation
import com.created.team201.data.mapper.toDomain
import com.created.team201.data.mapper.toRequestDto
import com.created.team201.data.remote.api.MyPageService
import javax.inject.Inject

class MyPageRepository @Inject constructor(
    private val myPageService: MyPageService,
) {

    suspend fun getMyPage(): Result<Profile> =
        runCatching {
            myPageService.getMyPage().toDomain()
        }

    suspend fun getAvailableNickname(nickname: Nickname): Result<Boolean> =
        runCatching {
            myPageService.getAvailableNickname(nickname.nickname).exists
        }

    suspend fun patchMyProfile(profileInformation: ProfileInformation): Result<Unit> =
        runCatching {
            myPageService.patchMyProfile(profileInformation.toRequestDto())
        }
}
