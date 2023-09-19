package com.created.team201.presentation.home.model

data class UserStudy(
    val studyId: Int,
    val isMaster: Boolean,
    val studyName: String,
    val dDay: Int,
    val mustDo: String,
    val grassSeed: Int
)
