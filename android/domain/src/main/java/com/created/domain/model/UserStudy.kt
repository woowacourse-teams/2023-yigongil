package com.created.domain.model

data class UserStudy(
    val studyId: Long,
    val studyName: String,
    val mustDo: String,
    val leftDays: Int,
    val grassSeedsCount: Int,
    val isMaster: Boolean
)
