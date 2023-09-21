package com.created.team201.data.datasource.remote

import com.created.domain.model.Nickname
import com.created.domain.model.ProfileInformation
import com.created.team201.data.remote.response.MyPageResponseDto
import com.created.team201.data.remote.response.NicknameResponseDto

interface MyPageDataSource {

    suspend fun getMyPage(): MyPageResponseDto
    suspend fun getAvailableNickname(nickname: Nickname): NicknameResponseDto
    suspend fun patchMyProfile(profileInformation: ProfileInformation)
}
