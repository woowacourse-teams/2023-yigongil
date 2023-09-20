package com.created.team201.data.datasource.remote

import com.created.domain.model.Nickname
import com.created.domain.model.ProfileInformation
import com.created.team201.data.mapper.toRequestDto
import com.created.team201.data.remote.api.MyPageService
import com.created.team201.data.remote.response.MyPageResponseDto
import com.created.team201.data.remote.response.NicknameResponseDto

class MyPageDataSourceImpl(
    private val myPageService: MyPageService,
) : MyPageDataSource {
    override suspend fun getMyPage(): MyPageResponseDto {
        return myPageService.getMyPage()
    }

    override suspend fun getAvailableNickname(nickname: Nickname): NicknameResponseDto {
        return myPageService.getAvailableNickname(nickname.nickname)
    }

    override suspend fun patchMyProfile(profileInformation: ProfileInformation) {
        return myPageService.patchMyProfile(profileInformation.toRequestDto())
    }
}
