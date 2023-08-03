package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FinishedStudyResponseDto(
    @SerialName("id")
    val id: Long,
    @SerialName("averageTier")
    val averageTier: Int,
    @SerialName("isSucceed")
    val isSucceed: Boolean,
    @SerialName("name")
    val name: String,
    @SerialName("numberOfCurrentMembers")
    val numberOfCurrentMembers: Int,
    @SerialName("numberOfMaximumMembers")
    val numberOfMaximumMembers: Int,
    @SerialName("periodOfRound")
    val periodOfRound: String,
    @SerialName("startAt")
    val startAt: String,
    @SerialName("totalRoundCount")
    val totalRoundCount: Int,
)
