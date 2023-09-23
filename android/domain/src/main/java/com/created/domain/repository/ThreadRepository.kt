package com.created.domain.repository

import com.created.domain.model.Feeds
import com.created.domain.model.MustDo
import kotlinx.coroutines.flow.Flow

interface ThreadRepository {

    suspend fun postFeeds(studyId: Long, content: String, image: String?): Result<Unit>

    fun getMustDo(studyId: Long): Flow<List<MustDo>>

    fun getFeeds(studyId: Long): Flow<List<Feeds>>
}