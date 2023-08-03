package com.created.domain.repository

import com.created.domain.model.CreateStudy

interface CreateStudyRepository {
    suspend fun createStudy(createStudy: CreateStudy): Result<Long>
}
