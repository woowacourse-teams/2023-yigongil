package com.created.team201.data.mapper

import com.created.domain.model.Feeds
import com.created.domain.model.MustDo
import com.created.team201.data.remote.response.FeedsResponseDto
import com.created.team201.data.remote.response.MustDoCertificationResponseDto

fun MustDoCertificationResponseDto.Me.toMustDo(): MustDo = MustDo(
    id = id,
    isCertified = isCertified,
    nickname = nickname,
    profileImageUrl = profileImageUrl
)

fun MustDoCertificationResponseDto.Other.toMustDo(): MustDo = MustDo(
    id = id,
    isCertified = isCertified,
    nickname = nickname,
    profileImageUrl = profileImageUrl
)

fun FeedsResponseDto.toFeeds(): Feeds = Feeds(
    author = this.author.toAuthor(),
    content = content,
    createdAt = createdAt,
    id = id.toLong(),
    imageUrl = imageUrl ?: ""
    // 서버 널 확인
)

fun FeedsResponseDto.Author.toAuthor(): Feeds.Author = Feeds.Author(
    id = id.toLong(), nickname = nickname, profileImageUrl = profileImageUrl
)
