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
    val averageTier: Int,
    @SerialName("name")
    val name: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("minimumWeeks")
    val minimumWeeks: Int,
    @SerialName("meetingDaysPerWeek")
    val meetingDaysCountPerWeek: Int,
    @SerialName("numberOfCurrentMembers")
    val numberOfCurrentMembers: Int,
    @SerialName("numberOfMaximumMembers")
    val numberOfMaximumMembers: Int,
)
