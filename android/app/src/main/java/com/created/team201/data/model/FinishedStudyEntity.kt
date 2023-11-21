package com.created.team201.data.model

data class FinishedStudyEntity(
    val id: Long,
    val name: String,
    val averageTier: Int,
    val numberOfCurrentMembers: Int,
    val numberOfMaximumMembers: Int,
    val isSucceed: Boolean,
)
