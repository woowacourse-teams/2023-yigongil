package com.created.team201.data.model

data class UserStudyEntity(
    val studyId: Long,
    val studyName: String,
    val mustDo: String,
    val leftDays: Int,
    val grassSeedsCount: Int,
    val isMaster: Boolean,
)
