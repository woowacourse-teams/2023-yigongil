package com.created.team201.data.remote

import com.created.team201.data.remote.api.AuthService
import com.created.team201.data.remote.api.HomeService
import com.created.team201.data.remote.api.MyPageService
import com.created.team201.data.remote.api.OnBoardingService
import com.created.team201.data.remote.api.ProfileService
import com.created.team201.data.remote.api.StudyDetailService
import com.created.team201.data.remote.api.StudyListService
import com.created.team201.data.remote.api.StudyManageService
import com.created.team201.data.remote.api.StudyManagementService
import com.created.team201.data.remote.api.UpdateStudyService

object NetworkServiceModule {
    val onBoardingService by lazy { NetworkModule.create<OnBoardingService>() }
    val studyListService by lazy { NetworkModule.create<StudyListService>() }
    val updateStudyService by lazy { NetworkModule.create<UpdateStudyService>() }
    val studyManageService by lazy { NetworkModule.create<StudyManageService>() }
    val studyManagementService by lazy { NetworkModule.create<StudyManagementService>() }
    val homeService by lazy { NetworkModule.create<HomeService>() }
    val myPageService by lazy { NetworkModule.create<MyPageService>() }
    val studyDetailService by lazy { NetworkModule.create<StudyDetailService>() }
    val profileService by lazy { NetworkModule.create<ProfileService>() }
    val authService by lazy { NetworkModule.create<AuthService>() }
}
