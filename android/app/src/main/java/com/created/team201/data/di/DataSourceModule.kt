package com.created.team201.data.di

import com.created.team201.data.datasource.local.DefaultOnBoardingDataSource
import com.created.team201.data.datasource.local.DefaultTokenDataSource
import com.created.team201.data.datasource.local.OnBoardingDataSource
import com.created.team201.data.datasource.local.TokenDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Singleton
    @Binds
    fun bindDefaultOnBoardingDataSource(defaultOnBoardingDataSource: DefaultOnBoardingDataSource): OnBoardingDataSource

    @Singleton
    @Binds
    fun bindDefaultTokenDataSource(defaultTokenDataSource: DefaultTokenDataSource): TokenDataSource
}
