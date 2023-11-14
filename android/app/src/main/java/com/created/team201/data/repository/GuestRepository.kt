package com.created.team201.data.repository

import com.created.team201.data.datasource.local.OnBoardingDataSource
import com.created.team201.data.datasource.local.TokenDataSource
import javax.inject.Inject

class GuestRepository @Inject constructor(
    private val tokenDataSource: TokenDataSource,
    private val onBoardingDataSource: OnBoardingDataSource,
) {

    fun signUpGuest() {
        tokenDataSource.setAccessToken("")
        tokenDataSource.setRefreshToken("")
        onBoardingDataSource.setOnBoardingIsDone(false)
    }

    fun getIsGuest(): Boolean {
        return tokenDataSource.getIsGuest()
    }
}
