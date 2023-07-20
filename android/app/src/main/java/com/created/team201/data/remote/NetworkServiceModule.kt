package com.created.team201.data.remote

import com.created.team201.data.remote.api.CreateStudyService

object NetworkServiceModule {
    val createStudyService = NetworkModule.create<CreateStudyService>()
}
