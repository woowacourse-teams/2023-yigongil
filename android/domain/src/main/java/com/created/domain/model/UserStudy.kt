package com.created.domain.model

data class UserStudy(
    val userId: Int,
    val name: String,
    val mustDo: String,
    val leftDays: Int,
    val grassSeedsCount: Int,
    val isMaster: Boolean
)
