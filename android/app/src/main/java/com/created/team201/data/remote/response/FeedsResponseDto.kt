package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedsResponseDto(
    @SerialName("id")
    val id: Int,
    @SerialName("author")
    val author: AuthorResponseDto,
    @SerialName("imageUrl")
    val imageUrl: String?,
    @SerialName("content")
    val content: String,
    @SerialName("createdAt")
    val createdAt: String,
)
