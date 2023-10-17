package com.created.domain.model

data class Feeds(
    val author: Author,
    val content: String,
    val createdAt: String,
    val id: Long,
    val imageUrl: String
) {

    data class Author(
        val id: Long,
        val nickname: String,
        val profileImageUrl: String
    )
}
