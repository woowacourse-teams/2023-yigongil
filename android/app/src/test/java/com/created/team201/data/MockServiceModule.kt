package com.created.team201.data

import com.created.team201.data.remote.api.CreateStudyService

object MockServiceModule {
    val createStudyService = MockNetworkModule.create<CreateStudyService>()
}
