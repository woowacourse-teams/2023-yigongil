package com.created.team201.data.mapper

import com.created.domain.model.Author
import com.created.domain.model.Feeds
import com.created.domain.model.MustDo
import com.created.domain.model.MustDoCertification
import com.created.team201.data.remote.response.AuthorResponseDto
import com.created.team201.data.remote.response.FeedsResponseDto
import com.created.team201.data.remote.response.MustDoCertificationResponseDto

fun MustDoCertificationResponseDto.toDomain(): MustDoCertification = MustDoCertification(
    studyName = studyName,
    upcomingRound = upcomingRound.toUpcomingRound(),
    me = me.toMustDo(),
    others = others.map { it.toMustDo() },

)

fun MustDoCertificationResponseDto.UpcomingRound.toUpcomingRound(): MustDoCertification.UpcomingRound =
    MustDoCertification.UpcomingRound(
        id = id,
        weekNumber = weekNumber,

    )

fun MustDoCertificationResponseDto.User.toMustDo(): MustDo = MustDo(
    id = id,
    isCertified = isCertified,
    nickname = nickname,
    profileImageUrl = profileImageUrl,
)

fun FeedsResponseDto.toFeeds(): Feeds = Feeds(
    author = this.author.toAuthor(),
    content = content,
    createdAt = createdAt,
    id = id.toLong(),
    imageUrl = imageUrl ?: "",
    // 서버 널 확인
)

fun AuthorResponseDto.toAuthor(): Author = Author(
    id = id,
    nickname = nickname,
    profileImageUrl = profileImageUrl,
)
