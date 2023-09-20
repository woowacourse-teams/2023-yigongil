package com.created.domain.repository

import com.created.domain.model.UserStudy

interface HomeRepository {
    suspend fun getUserStudies(): List<UserStudy>
}
