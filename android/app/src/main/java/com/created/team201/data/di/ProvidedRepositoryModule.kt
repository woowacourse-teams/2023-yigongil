package com.created.team201.data.di

import com.created.team201.data.remote.api.UserStudyService
import com.created.team201.data.repository.UserStudyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProvidedRepositoryModule {

    @Provides
    @Singleton
    fun bindUserStudyRepository(userStudyService: UserStudyService): UserStudyRepository =
        UserStudyRepository(userStudyService)
}