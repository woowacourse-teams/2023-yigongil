package com.created.team201.data.datasource.remote

import com.created.domain.model.CreateStudy

interface CreateStudyDataSource {
    suspend fun createStudy(createStudy: CreateStudy)
}
