package com.created.team201.data.remote

import com.created.team201.data.remote.api.AuthService
import com.created.team201.data.remote.api.CreateStudyService
import com.created.team201.data.remote.api.HomeService
import com.created.team201.data.remote.api.MyPageService
import com.created.team201.data.remote.api.ProfileService
import com.created.team201.data.remote.api.StudyDetailService
import com.created.team201.data.remote.api.StudyListService
import com.created.team201.data.remote.api.StudyManageService

object NetworkServiceModule {
    val studyListService by lazy { NetworkModule.create<StudyListService>() }
    val createStudyService by lazy { NetworkModule.create<CreateStudyService>() }
    val studyManageService by lazy { NetworkModule.create<StudyManageService>() }
    val homeService by lazy { NetworkModule.create<HomeService>() }
    val myPageService by lazy { NetworkModule.create<MyPageService>() }
    val studyDetailService by lazy { NetworkModule.create<StudyDetailService>() }
    val profileService by lazy { NetworkModule.create<ProfileService>() }
    val authService by lazy { NetworkModule.create<AuthService>() }
}
