package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedsResponseDto(
    @SerialName("author")
    val author: Author,
    @SerialName("content")
    val content: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("id")
    val id: Int,
    @SerialName("imageUrl")
    val imageUrl: String?
) {
    @Serializable
    data class Author(
        @SerialName("id")
        val id: Int,
        @SerialName("nickname")
        val nickname: String,
        @SerialName("profileImageUrl")
        val profileImageUrl: String
    )
}

