package com.created.team201.data.di

import com.created.team201.data.di.qualifier.AuthRetrofit
import com.created.team201.data.di.qualifier.DefaultRetrofit
import com.created.team201.data.remote.api.AuthService
import com.created.team201.data.remote.api.CertificationService
import com.created.team201.data.remote.api.CommonStudyListService
import com.created.team201.data.remote.api.CreateStudyService
import com.created.team201.data.remote.api.HomeService
import com.created.team201.data.remote.api.MemberStudyListService
import com.created.team201.data.remote.api.MyPageService
import com.created.team201.data.remote.api.OnBoardingService
import com.created.team201.data.remote.api.ProfileService
import com.created.team201.data.remote.api.ReportService
import com.created.team201.data.remote.api.SettingService
import com.created.team201.data.remote.api.StudyDetailService
import com.created.team201.data.remote.api.ThreadService
import com.created.team201.data.remote.api.UpdateStudyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Singleton
    @Provides
    fun provideOnBoardingService(@AuthRetrofit retrofit: Retrofit): OnBoardingService =
        retrofit.create(OnBoardingService::class.java)

    @Singleton
    @Provides
    fun provideCommonStudyListService(@DefaultRetrofit retrofit: Retrofit): CommonStudyListService =
        retrofit.create(CommonStudyListService::class.java)

    @Singleton
    @Provides
    fun provideMemberStudyListService(@AuthRetrofit retrofit: Retrofit): MemberStudyListService =
        retrofit.create(MemberStudyListService::class.java)

    @Singleton
    @Provides
    fun provideCreateStudyService(@AuthRetrofit retrofit: Retrofit): CreateStudyService =
        retrofit.create(CreateStudyService::class.java)

    @Singleton
    @Provides
    fun provideUpdateStudyService(@AuthRetrofit retrofit: Retrofit): UpdateStudyService =
        retrofit.create(UpdateStudyService::class.java)

    @Singleton
    @Provides
    fun provideHomeService(@AuthRetrofit retrofit: Retrofit): HomeService =
        retrofit.create(HomeService::class.java)

    @Singleton
    @Provides
    fun provideMyPageService(@AuthRetrofit retrofit: Retrofit): MyPageService =
        retrofit.create(MyPageService::class.java)

    @Singleton
    @Provides
    fun provideStudyDetailService(@AuthRetrofit retrofit: Retrofit): StudyDetailService =
        retrofit.create(StudyDetailService::class.java)

    @Singleton
    @Provides
    fun provideProfileService(@AuthRetrofit retrofit: Retrofit): ProfileService =
        retrofit.create(ProfileService::class.java)

    @Singleton
    @Provides
    fun provideAuthService(@DefaultRetrofit retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Singleton
    @Provides
    fun provideReportService(@AuthRetrofit retrofit: Retrofit): ReportService =
        retrofit.create(ReportService::class.java)

    @Singleton
    @Provides
    fun provideSettingService(@AuthRetrofit retrofit: Retrofit): SettingService =
        retrofit.create(SettingService::class.java)

    @Singleton
    @Provides
    fun provideThreadService(@AuthRetrofit retrofit: Retrofit): ThreadService =
        retrofit.create(ThreadService::class.java)

    @Singleton
    @Provides
    fun provideCertificationService(@AuthRetrofit retrofit: Retrofit): CertificationService =
        retrofit.create(CertificationService::class.java)
}
