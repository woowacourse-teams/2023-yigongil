package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyDetailResponseDto(
    @SerialName("id")
    val id: Long,
    @SerialName("processingStatus")
    val processingStatus: Int,
    @SerialName("name")
    val name: String,
    @SerialName("numberOfCurrentMembers")
    val numberOfCurrentMembers: Int,
    @SerialName("numberOfMaximumMembers")
    val numberOfMaximumMembers: Int,
    @SerialName("studyMasterId")
    val studyMasterId: Long,
    @SerialName("role")
    val role: Int,
    @SerialName("startAt")
    val startAt: String,
    @SerialName("totalRoundCount")
    val totalRoundCount: Int,
    @SerialName("periodOfRound")
    val periodOfRound: String,
    @SerialName("currentRound")
    val currentRound: Int,
    @SerialName("introduction")
    val introduction: String,
    @SerialName("members")
    val members: List<MemberResponseDto>,
    @SerialName("rounds")
    val rounds: List<RoundResponseDto>,
)
