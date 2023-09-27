package com.created.team201.data.di

import android.content.Context
import com.created.team201.data.datasource.local.OnBoardingStorage
import com.created.team201.data.datasource.local.TokenStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Singleton
    @Provides
    fun provideTokenStorage(@ApplicationContext context: Context): TokenStorage =
        TokenStorage(context)

    @Singleton
    @Provides
    fun provideOnBoardingIsDoneStorage(@ApplicationContext context: Context): OnBoardingStorage =
        OnBoardingStorage(context)

}