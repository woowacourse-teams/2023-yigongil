package com.created.domain.repository

import com.created.domain.model.Feeds
import com.created.domain.model.MustDoCertification
import kotlinx.coroutines.flow.Flow

interface ThreadRepository {

    suspend fun updateFeeds(studyId: Long, content: String, image: String?)

    fun getFeeds(studyId: Long): Flow<List<Feeds>>

    suspend fun getMustDoCertification(studyId: Long): MustDoCertification

    fun postMustDoCertification(studyId: Long)

    fun getStudyInfo(studyId: Long)

    fun getMustDo(studyId: Long)
}