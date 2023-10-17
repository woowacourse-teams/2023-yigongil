package com.created.team201.data.repository

import com.created.domain.model.Feeds
import com.created.domain.model.MustDoCertification
import com.created.domain.repository.ThreadRepository
import com.created.team201.data.mapper.toDomain
import com.created.team201.data.mapper.toFeeds
import com.created.team201.data.remote.api.ThreadService
import com.created.team201.data.remote.request.FeedRequestDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DefaultThreadRepository @Inject constructor(
    private val threadService: ThreadService
) : ThreadRepository {

    override suspend fun getMustDoCertification(studyId: Long): MustDoCertification {
        val mustDoCertification = threadService.getMustDoCertification(studyId = studyId)

        return mustDoCertification.toDomain()
    }

    override fun getFeeds(studyId: Long): Flow<List<Feeds>> = flow {
        while (true) {
            val feeds = threadService.getFeeds(studyId).map { it.toFeeds() }

            emit(feeds)
            delay(1000)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateFeeds(studyId: Long, content: String, image: String?) {
        threadService.postFeeds(
            studyId,
            FeedRequestDto(
                content = content, imageUrl = image
            )
        )
    }

    override fun postMustDoCertification(studyId: Long) {
        TODO("Not yet implemented")
    }

    override fun getStudyInfo(studyId: Long) {
        TODO("Not yet implemented")
    }

    override fun getMustDo(studyId: Long) {
        TODO("Not yet implemented")
    }
}
