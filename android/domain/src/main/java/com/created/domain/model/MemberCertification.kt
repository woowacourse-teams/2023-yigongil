package com.created.domain.model

data class MemberCertification(
    val id: Long,
    val author: Author,
    val imageUrl: String,
    val content: String,
    val createdAt: String,
)
