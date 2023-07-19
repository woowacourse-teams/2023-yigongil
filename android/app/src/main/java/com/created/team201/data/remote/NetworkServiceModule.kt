package com.created.team201.data.remote

import com.created.team201.data.remote.api.HomeService

object NetworkServiceModule {
    val homeService = NetworkModule.create<HomeService>()
}
