package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponseDto(
    @SerialName("id")
    val id: Long,
    @SerialName("githubId")
    val githubId: String,
    @SerialName("introduction")
    val introduction: String?,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profileImageUrl")
    val profileImageUrl: String?,
    @SerialName("successRate")
    val successRate: Double,
    @SerialName("successfulRoundCount")
    val successfulRoundCount: Int,
    @SerialName("tier")
    val tier: Int,
    @SerialName("tierProgress")
    val tierProgress: Int,
    @SerialName("finishedStudies")
    val finishedStudies: List<FinishedStudyResponseDto>,
)
