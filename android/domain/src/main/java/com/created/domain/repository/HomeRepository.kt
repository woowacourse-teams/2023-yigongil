package com.created.domain.repository

import com.created.domain.model.UserInfo

interface HomeRepository {

    suspend fun getUserStudies(): UserInfo
}
