package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FinishedStudyResponseDto(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("averageTier")
    val averageTier: Int,
    @SerialName("numberOfCurrentMembers")
    val numberOfCurrentMembers: Int,
    @SerialName("numberOfMaximumMembers")
    val numberOfMaximumMembers: Int,
    @SerialName("isSucceed")
    val isSucceed: Boolean,
)
