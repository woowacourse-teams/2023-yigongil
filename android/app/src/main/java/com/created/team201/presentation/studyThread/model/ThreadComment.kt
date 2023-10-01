package com.created.team201.presentation.studyThread.model

data class ThreadComment(
    val userId: Int,
    val profile: String,
    val userName: String,
    val pastTime: String,
    val comment: String
)
