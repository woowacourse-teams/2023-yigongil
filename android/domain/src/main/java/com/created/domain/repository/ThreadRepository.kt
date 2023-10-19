package com.created.domain.repository

import com.created.domain.model.Feeds
import com.created.domain.model.MustDoCertification
import com.created.domain.model.WeeklyMustDo
import kotlinx.coroutines.flow.Flow

interface ThreadRepository {

    suspend fun updateFeeds(studyId: Long, content: String, image: String?)

    fun getFeeds(studyId: Long): Flow<List<Feeds>>

    suspend fun getMustDoCertification(studyId: Long): MustDoCertification

    fun postMustDoCertification(studyId: Long)

    fun getStudyInfo(studyId: Long)

    suspend fun getWeeklyMustDo(studyId: Long, weekNumber: Int): List<WeeklyMustDo>

    suspend fun putMustDo(roundId: Long, content: String)

    suspend fun endStudy(studyId: Long)

    suspend fun quitStudy(studyId: Long)
}
