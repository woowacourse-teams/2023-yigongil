package com.created.team201.data.datasource.remote

import com.created.domain.model.CreateStudy

interface CreateStudyRemoteDataSource {
    suspend fun createStudy(createStudy: CreateStudy)
}
