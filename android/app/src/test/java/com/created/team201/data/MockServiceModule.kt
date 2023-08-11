package com.created.team201.data

import com.created.team201.data.remote.api.MyPageService
import com.created.team201.data.remote.api.UpdateStudyService
import com.created.team201.data.remote.api.OnBoardingService

object MockServiceModule {
    val updateStudyService = MockNetworkModule.create<UpdateStudyService>()
    val myPageService = MockNetworkModule.create<MyPageService>()
    val onBoardingService = MockNetworkModule.create<OnBoardingService>()
}
