package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberResponseDto(
    @SerialName("id")
    val id: Long,
    @SerialName("tier")
    val tier: Int,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("successRate")
    val successRate: Double,
    @SerialName("profileImage")
    val profileImage: String?,
)
