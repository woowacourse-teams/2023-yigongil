package com.created.team201.data.di

import android.content.Context
import com.created.team201.data.datasource.local.OnBoardingStorage
import com.created.team201.data.datasource.local.TokenStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface StorageModule {

    @Singleton
    @Binds
    fun provideTokenStorage(@ApplicationContext context: Context): TokenStorage

    @Singleton
    @Binds
    fun provideOnBoardingIsDoneStorage(@ApplicationContext context: Context): OnBoardingStorage


}