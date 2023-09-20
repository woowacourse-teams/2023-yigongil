package com.created.domain.model

data class UserStudy(
    val isMaster: Boolean,
    val grassCount: Int,
    val id: Int,
    val leftDays: Int,
    val name: String,
    val todoContent: String
)
