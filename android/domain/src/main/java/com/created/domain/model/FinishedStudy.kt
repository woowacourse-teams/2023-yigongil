package com.created.domain.model

data class FinishedStudy(
    val id: Long,
    val name: String,
    val averageTier: Int,
    val numberOfCurrentMembers: Int,
    val numberOfMaximumMembers: Int,
    val isSucceed: Boolean,
)
