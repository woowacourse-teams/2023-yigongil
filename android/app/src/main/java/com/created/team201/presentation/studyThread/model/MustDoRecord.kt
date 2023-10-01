package com.created.team201.presentation.studyThread.model

data class MustDoRecord(
    val userId: Int,
    val userName: String,
    val isDone: Boolean,
    val recordLink: String
)
