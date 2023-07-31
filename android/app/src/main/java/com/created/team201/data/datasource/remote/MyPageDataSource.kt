package com.created.team201.data.datasource.remote

import com.created.team201.data.remote.response.MyPageResponseDto

interface MyPageDataSource {

    suspend fun getMyPage(): MyPageResponseDto
}
