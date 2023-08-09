package com.created.team201.data

import com.created.team201.data.remote.api.CreateStudyService
import com.created.team201.data.remote.api.MyPageService
import com.created.team201.data.remote.api.OnBoardingService

object MockServiceModule {
    val createStudyService = MockNetworkModule.create<CreateStudyService>()
    val myPageService = MockNetworkModule.create<MyPageService>()
    val onBoardingService = MockNetworkModule.create<OnBoardingService>()
}
