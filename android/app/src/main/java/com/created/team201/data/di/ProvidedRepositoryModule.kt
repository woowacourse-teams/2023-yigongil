package com.created.team201.data.di

import com.created.team201.data.datasource.local.OnBoardingDataSource
import com.created.team201.data.datasource.local.TokenDataSource
import com.created.team201.data.remote.api.AuthService
import com.created.team201.data.remote.api.CertificationService
import com.created.team201.data.remote.api.CommonStudyListService
import com.created.team201.data.remote.api.CreateStudyService
import com.created.team201.data.remote.api.ImageService
import com.created.team201.data.remote.api.MemberStudyListService
import com.created.team201.data.remote.api.MyPageService
import com.created.team201.data.remote.api.OnBoardingService
import com.created.team201.data.remote.api.ProfileService
import com.created.team201.data.remote.api.ReportService
import com.created.team201.data.remote.api.SettingService
import com.created.team201.data.remote.api.StudyDetailService
import com.created.team201.data.remote.api.ThreadService
import com.created.team201.data.remote.api.UserStudyService
import com.created.team201.data.repository.AuthRepository
import com.created.team201.data.repository.CertificationRepository
import com.created.team201.data.repository.CreateStudyRepository
import com.created.team201.data.repository.GuestRepository
import com.created.team201.data.repository.MyPageRepository
import com.created.team201.data.repository.OnBoardingRepository
import com.created.team201.data.repository.ProfileRepository
import com.created.team201.data.repository.ReportRepository
import com.created.team201.data.repository.SettingRepository
import com.created.team201.data.repository.SplashRepository
import com.created.team201.data.repository.StudyDetailRepository
import com.created.team201.data.repository.StudyListRepository
import com.created.team201.data.repository.ThreadRepository
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
    fun providesUserStudyRepository(
        userStudyService: UserStudyService,
    ): UserStudyRepository = UserStudyRepository(userStudyService)

    @Provides
    @Singleton
    fun providesAuthRepository(
        authService: AuthService,
        tokenDataSource: TokenDataSource,
    ): AuthRepository = AuthRepository(authService, tokenDataSource)

    @Provides
    @Singleton
    fun providesCertificationRepository(
        imageService: ImageService,
        certificationService: CertificationService,
    ): CertificationRepository = CertificationRepository(imageService, certificationService)

    @Provides
    @Singleton
    fun providesCreateStudyRepository(
        createStudyService: CreateStudyService,
    ): CreateStudyRepository = CreateStudyRepository(createStudyService)

    @Provides
    @Singleton
    fun providesGuestRepository(
        tokenDataSource: TokenDataSource,
        onBoardingDataSource: OnBoardingDataSource,
    ): GuestRepository = GuestRepository(tokenDataSource, onBoardingDataSource)

    @Provides
    @Singleton
    fun providesMyPageRepository(
        myPageService: MyPageService,
    ): MyPageRepository = MyPageRepository(myPageService)

    @Provides
    @Singleton
    fun providesOnBoardingRepository(
        onBoardingDataSource: OnBoardingDataSource,
        onBoardingService: OnBoardingService,
    ): OnBoardingRepository = OnBoardingRepository(onBoardingDataSource, onBoardingService)

    @Provides
    @Singleton
    fun providesProfileRepository(
        profileService: ProfileService,
    ): ProfileRepository = ProfileRepository(profileService)

    @Provides
    @Singleton
    fun providesReportRepository(
        reportService: ReportService,
    ): ReportRepository = ReportRepository(reportService)

    @Provides
    @Singleton
    fun providesSettingRepository(
        onBoardingDataSource: OnBoardingDataSource,
        settingService: SettingService,
        tokenDataSource: TokenDataSource,
    ): SettingRepository = SettingRepository(onBoardingDataSource, settingService, tokenDataSource)

    @Provides
    @Singleton
    fun providesSplashRepository(): SplashRepository = SplashRepository()

    @Provides
    @Singleton
    fun providesStudyDetailRepository(
        studyDetailService: StudyDetailService,
    ): StudyDetailRepository = StudyDetailRepository(studyDetailService)

    @Provides
    @Singleton
    fun providesStudyListRepository(
        commonStudyListService: CommonStudyListService,
        memberStudyListService: MemberStudyListService,
    ): StudyListRepository = StudyListRepository(commonStudyListService, memberStudyListService)

    @Provides
    @Singleton
    fun providesThreadRepository(
        threadService: ThreadService,
    ): ThreadRepository = ThreadRepository(threadService)
}
