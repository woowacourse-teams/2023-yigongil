package com.created.team201.data.remote.api

import com.created.team201.data.remote.response.MyPageResponseDto
import retrofit2.http.GET

interface MyPageService {
    @GET("/v1/members/my")
    suspend fun getMyPage(): MyPageResponseDto
}
