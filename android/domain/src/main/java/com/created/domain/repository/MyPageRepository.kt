package com.created.domain.repository

import com.created.domain.model.Profile

interface MyPageRepository {

    suspend fun getMyPage(): Result<Profile>
}
