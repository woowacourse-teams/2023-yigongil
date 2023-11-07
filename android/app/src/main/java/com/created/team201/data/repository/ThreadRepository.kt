package com.created.team201.data.repository

import com.created.domain.model.Feeds
import com.created.domain.model.MustDoCertification
import com.created.domain.model.WeeklyMustDo
import com.created.team201.data.mapper.toDomain
import com.created.team201.data.mapper.toFeeds
import com.created.team201.data.remote.api.ThreadService
import com.created.team201.data.remote.request.FeedRequestDto
import com.created.team201.data.remote.request.MustDoContentRequestDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ThreadRepository @Inject constructor(
    private val threadService: ThreadService,
) {

    suspend fun getMustDoCertification(studyId: Long): MustDoCertification {
        val mustDoCertification = threadService.getMustDoCertification(studyId = studyId)

        return mustDoCertification.toDomain()
    }

    fun getFeeds(studyId: Long): Flow<List<Feeds>> = flow {
        while (true) {
            val feeds = threadService.getFeeds(studyId).map { it.toFeeds() }

            emit(feeds)
            delay(1000)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun updateFeeds(studyId: Long, content: String, image: String?) {
        threadService.postFeeds(
            studyId,
            FeedRequestDto(
                content = content,
                imageUrl = image,
            ),
        )
    }

    fun postMustDoCertification(studyId: Long) {
        TODO("Not yet implemented")
    }

    fun getStudyInfo(studyId: Long) {
        TODO("Not yet implemented")
    }

    suspend fun getWeeklyMustDo(studyId: Long, weekNumber: Int): List<WeeklyMustDo> {
        return threadService.getMustDos(studyId, weekNumber).map { it.toDomain() }
    }

    suspend fun putMustDo(roundId: Long, content: String) {
        threadService.putMustDo(roundId, MustDoContentRequestDto(content))
    }

    suspend fun endStudy(studyId: Long) {
        threadService.endStudy(studyId)
    }

    suspend fun quitStudy(studyId: Long) {
        threadService.quitStudy(studyId)
    }
}
