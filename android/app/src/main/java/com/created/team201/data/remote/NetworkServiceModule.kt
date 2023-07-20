package com.created.team201.data.remote

import com.created.team201.data.remote.api.CreateStudyService
import com.created.team201.data.remote.api.HomeService
import com.created.team201.data.remote.api.StudyDetailService

object NetworkServiceModule {
    val createStudyService = NetworkModule.create<CreateStudyService>()
    val homeService = NetworkModule.create<HomeService>()
    val studyDetailService = NetworkModule.create<StudyDetailService>()
}
