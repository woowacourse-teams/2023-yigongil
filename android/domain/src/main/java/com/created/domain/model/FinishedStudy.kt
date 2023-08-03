package com.created.domain.model

data class FinishedStudy(
    val id: Long,
    val averageTier: Int,
    val isSucceed: Boolean,
    val name: String,
    val numberOfCurrentMembers: Int,
    val numberOfMaximumMembers: Int,
    val periodOfRound: Period,
    val startAt: String,
    val totalRoundCount: Int,
)
