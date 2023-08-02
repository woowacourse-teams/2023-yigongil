package com.created.domain.model

data class StudyMember(
    val id: Long,
    val nickname: String,
    val profileImageUrl: String,
    val isDone: Boolean,
)
