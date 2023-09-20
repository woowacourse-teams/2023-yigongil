package com.created.team201.data.repository

import com.created.domain.repository.HomeRepository
import com.created.team201.data.remote.api.HomeService
import javax.inject.Inject

class DefaultHomeRepository @Inject constructor(
    private val homeService: HomeService,
) : HomeRepository
