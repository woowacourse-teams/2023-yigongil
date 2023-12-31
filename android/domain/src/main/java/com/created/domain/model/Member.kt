package com.created.domain.model

data class Member(
    val id: Long,
    val tier: Int,
    val isDeleted: Boolean,
    val nickname: String,
    val successRate: Double,
    val profileImage: String,
)
