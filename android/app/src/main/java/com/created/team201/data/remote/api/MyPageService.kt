package com.created.team201.data.remote.api

import com.created.team201.data.remote.request.MyProfileRequestDTO
import com.created.team201.data.remote.response.MyPageResponseDto
import com.created.team201.data.remote.response.NicknameResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface MyPageService {
    @GET("v1/members/my")
    suspend fun getMyPage(): MyPageResponseDto

    @GET("v1/members/exists?")
    suspend fun getAvailableNickname(
        @Query("nickname") nickname: String,
    ): NicknameResponseDto

    @PATCH("v1/members")
    suspend fun patchMyProfile(
        @Body profile: MyProfileRequestDTO
    )
}
