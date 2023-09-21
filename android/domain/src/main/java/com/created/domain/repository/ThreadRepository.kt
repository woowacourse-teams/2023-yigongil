package com.created.domain.repository

import com.created.domain.model.MustDo
import kotlinx.coroutines.flow.Flow

interface ThreadRepository {

    fun getMustDo(studyId: Long): Flow<List<MustDo>>
}