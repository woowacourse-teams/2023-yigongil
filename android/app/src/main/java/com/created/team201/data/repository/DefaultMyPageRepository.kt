package com.created.team201.data.repository

import com.created.domain.model.Nickname
import com.created.domain.model.Profile
import com.created.domain.model.ProfileInformation
import com.created.domain.repository.MyPageRepository
import com.created.team201.data.datasource.remote.MyPageDataSource
import com.created.team201.data.mapper.toDomain

class DefaultMyPageRepository(
    private val myPageDataSource: MyPageDataSource,
) : MyPageRepository {
    override suspend fun getMyPage(): Result<Profile> =
        runCatching {
            myPageDataSource.getMyPage().toDomain()
        }

    override suspend fun getAvailableNickname(nickname: Nickname): Result<Boolean> =
        runCatching {
            myPageDataSource.getAvailableNickname(nickname).exists
        }

    override suspend fun patchMyProfile(profileInformation: ProfileInformation): Result<Unit> =
        runCatching {
            myPageDataSource.patchMyProfile(profileInformation)
        }
}
