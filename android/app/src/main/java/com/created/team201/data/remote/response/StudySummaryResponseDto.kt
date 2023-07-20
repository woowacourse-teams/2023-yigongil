package com.created.team201.data.remote.response

import com.created.domain.PeriodUnit
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
    val period: PeriodUnit,
    @SerialName("numberOfCurrentMembers")
    val currentMember: Int,
    @SerialName("numberOfMaximumMembers")
    val maximumMember: Int,
)
