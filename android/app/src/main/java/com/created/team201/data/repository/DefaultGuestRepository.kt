package com.created.team201.data.repository

import com.created.domain.repository.GuestRepository
import com.created.team201.data.datasource.local.TokenDataSource

class DefaultGuestRepository(
    private val tokenDataSource: TokenDataSource,
) : GuestRepository {
    override fun signUpGuest() {
        tokenDataSource.setAccessToken("")
        tokenDataSource.setRefreshToken("")
    }

    override fun getIsGuest(): Boolean {
        return tokenDataSource.getIsGuest()
    }
}
