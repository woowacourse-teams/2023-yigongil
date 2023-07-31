package com.created.team201.data.repository

import com.created.domain.model.Profile
import com.created.domain.repository.MyPageRepository
import com.created.team201.data.datasource.remote.MyPageDataSource
import com.created.team201.data.mapper.toDomain

class MyPageRepositoryImpl(
    private val myPageDataSource: MyPageDataSource,
) : MyPageRepository {
    override suspend fun getMyPage(): Result<Profile> =
        runCatching {
            myPageDataSource.getMyPage().toDomain()
        }
}
