package com.created.team201.data

import com.created.team201.data.remote.api.MyPageService
import com.created.team201.data.remote.api.UpdateStudyService

object MockServiceModule {
    val updateStudyService = MockNetworkModule.create<UpdateStudyService>()
    val myPageService = MockNetworkModule.create<MyPageService>()
}
