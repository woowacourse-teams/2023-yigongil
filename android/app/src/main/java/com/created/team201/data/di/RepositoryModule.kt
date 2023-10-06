package com.created.team201.data.di

import com.created.domain.repository.AuthRepository
import com.created.domain.repository.CreateStudyRepository
import com.created.domain.repository.GuestRepository
import com.created.domain.repository.HomeRepository
import com.created.domain.repository.MyPageRepository
import com.created.domain.repository.OnBoardingRepository
import com.created.domain.repository.ProfileRepository
import com.created.domain.repository.ReportRepository
import com.created.domain.repository.SettingRepository
import com.created.domain.repository.StudyDetailRepository
import com.created.domain.repository.StudyListRepository
import com.created.domain.repository.UpdateStudyRepository
import com.created.team201.data.repository.DefaultAuthRepository
import com.created.team201.data.repository.DefaultCreateStudyRepository
import com.created.team201.data.repository.DefaultGuestRepository
import com.created.team201.data.repository.DefaultHomeRepository
import com.created.team201.data.repository.DefaultMyPageRepository
import com.created.team201.data.repository.DefaultOnBoardingRepository
import com.created.team201.data.repository.DefaultProfileRepository
import com.created.team201.data.repository.DefaultReportRepository
import com.created.team201.data.repository.DefaultSettingRepository
import com.created.team201.data.repository.DefaultStudyDetailRepository
import com.created.team201.data.repository.DefaultStudyListRepository
import com.created.team201.data.repository.DefaultUpdateStudyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindDefaultAuthRepository(defaultAuthRepository: DefaultAuthRepository): AuthRepository

    @Binds
    @Singleton
    fun bindDefaultGuestRepository(defaultGuestRepository: DefaultGuestRepository): GuestRepository

    @Binds
    @Singleton
    fun bindDefaultHomeRepository(defaultHomeRepository: DefaultHomeRepository): HomeRepository

    @Binds
    @Singleton
    fun bindDefaultMyPageRepository(defaultMyPageRepository: DefaultMyPageRepository): MyPageRepository

    @Binds
    @Singleton
    fun bindDefaultOnBoardingRepository(defaultOnBoardingRepository: DefaultOnBoardingRepository): OnBoardingRepository

    @Binds
    @Singleton
    fun bindDefaultProfileRepository(defaultProfileRepository: DefaultProfileRepository): ProfileRepository

    @Binds
    @Singleton
    fun bindDefaultReportRepository(defaultReportRepository: DefaultReportRepository): ReportRepository

    @Binds
    @Singleton
    fun bindDefaultSettingRepository(defaultSettingRepository: DefaultSettingRepository): SettingRepository

    @Binds
    @Singleton
    fun bindDefaultStudyDetailRepository(defaultStudyDetailRepository: DefaultStudyDetailRepository): StudyDetailRepository

    @Binds
    @Singleton
    fun bindDefaultStudyListRepository(defaultStudyListRepository: DefaultStudyListRepository): StudyListRepository

    @Binds
    @Singleton
    fun bindDefaultCreateStudyRepository(defaultCreateStudyRepository: DefaultCreateStudyRepository): CreateStudyRepository

    @Binds
    @Singleton
    fun bindDefaultUpdateStudyRepository(defaultUpdateStudyRepository: DefaultUpdateStudyRepository): UpdateStudyRepository
}
