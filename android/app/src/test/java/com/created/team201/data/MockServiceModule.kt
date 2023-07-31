package com.created.team201.data

import com.created.team201.data.remote.api.CreateStudyService
import com.created.team201.data.remote.api.MyPageService

object MockServiceModule {
    val createStudyService = MockNetworkModule.create<CreateStudyService>()
    val myPageService = MockNetworkModule.create<MyPageService>()
}
