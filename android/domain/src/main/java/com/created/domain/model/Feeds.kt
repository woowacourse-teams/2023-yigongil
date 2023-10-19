package com.created.domain.model

data class Feeds(
    val author: Author,
    val content: String,
    val createdAt: String,
    val id: Long,
    val imageUrl: String,
)
