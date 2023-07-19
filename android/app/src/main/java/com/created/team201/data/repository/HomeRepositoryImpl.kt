package com.created.team201.data.repository

import com.created.domain.model.UserInfo
import com.created.domain.repository.HomeRepository
import com.created.team201.data.datasource.remote.HomeDataSource
import com.created.team201.data.mapper.toDomain

class HomeRepositoryImpl(
    private val homeDataSource: HomeDataSource,
) : HomeRepository {
    override suspend fun getUserStudies(): UserInfo {
        return homeDataSource.getUserStudies().toDomain()
    }
}
