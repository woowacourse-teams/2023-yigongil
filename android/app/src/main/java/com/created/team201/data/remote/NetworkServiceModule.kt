package com.created.team201.data.remote

import com.created.team201.data.remote.api.CreateStudyService
import com.created.team201.data.remote.api.HomeService
import com.created.team201.data.remote.api.MyPageService
import com.created.team201.data.remote.api.StudyDetailService
import com.created.team201.data.remote.api.ProfileService
import com.created.team201.data.remote.api.StudyListService
import com.created.team201.data.remote.api.StudyManageService

object NetworkServiceModule {
    val studyListService = NetworkModule.create<StudyListService>()
    val createStudyService = NetworkModule.create<CreateStudyService>()
    val studyManageService = NetworkModule.create<StudyManageService>()
    val homeService = NetworkModule.create<HomeService>()
    val myPageService = NetworkModule.create<MyPageService>()
    val studyDetailService = NetworkModule.create<StudyDetailService>()
    val profileService = NetworkModule.create<ProfileService>()
}
