package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudySummaryResponseDto(
    @SerialName("id")
    val id: Long,
    @SerialName("processingStatus")
    val processingStatus: Int,
    @SerialName("averageTier")
    val tier: Int,
    @SerialName("name")
    val title: String,
    @SerialName("startAt")
    val date: String,
    @SerialName("totalRoundCount")
    val totalRound: Int,
    @SerialName("periodOfRound")
    val period: String,
    @SerialName("numberOfCurrentMembers")
    val currentMember: Int,
    @SerialName("numberOfMaximumMembers")
    val maximumMember: Int,
)
