package com.created.domain.repository

import com.created.domain.model.Feeds
import com.created.domain.model.MustDo
import kotlinx.coroutines.flow.Flow

interface ThreadRepository {

    suspend fun postFeeds(studyId: Long, content: String, image: String?): Result<Unit>

    fun getFeeds(studyId: Long): Flow<List<Feeds>>

    fun getMustDoCertification(studyId: Long): Flow<List<MustDo>>

    fun postMustDoCertification(studyId: Long)

    fun getStudyInfo(studyId: Long)

    fun getMustDo(studyId: Long)
}