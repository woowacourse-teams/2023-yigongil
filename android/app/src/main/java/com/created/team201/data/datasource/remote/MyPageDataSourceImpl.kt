package com.created.team201.data.datasource.remote

import com.created.team201.data.remote.api.MyPageService
import com.created.team201.data.remote.response.MyPageResponseDto

class MyPageDataSourceImpl(
    private val myPageService: MyPageService,
) : MyPageDataSource {
    override suspend fun getMyPage(): MyPageResponseDto {
        return myPageService.getMyPage()
    }
}
