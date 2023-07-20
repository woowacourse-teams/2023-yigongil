package com.created.team201.data.remote

import com.created.team201.data.remote.api.CreateStudyService
import com.created.team201.data.remote.api.HomeService

object NetworkServiceModule {
    val createStudyService = NetworkModule.create<CreateStudyService>()
    val homeService = NetworkModule.create<HomeService>()
}
