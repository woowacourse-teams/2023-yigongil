package com.created.team201.data.repository

import com.created.domain.repository.HomeRepository
import com.created.team201.data.remote.api.HomeService

class DefaultHomeRepository(
    private val homeService: HomeService,
) : HomeRepository {

}
