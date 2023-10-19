package com.created.team201.data.repository

import com.created.domain.repository.GuestRepository
import com.created.team201.data.datasource.local.OnBoardingDataSource
import com.created.team201.data.datasource.local.TokenDataSource
import javax.inject.Inject

class DefaultGuestRepository @Inject constructor(
    private val tokenDataSource: TokenDataSource,
    private val onBoardingDataSource: OnBoardingDataSource,
) : GuestRepository {
    override fun signUpGuest() {
        tokenDataSource.setAccessToken("")
        tokenDataSource.setRefreshToken("")
        onBoardingDataSource.setOnBoardingIsDone(false)
    }

    override fun getIsGuest(): Boolean {
        return tokenDataSource.getIsGuest()
    }
}
