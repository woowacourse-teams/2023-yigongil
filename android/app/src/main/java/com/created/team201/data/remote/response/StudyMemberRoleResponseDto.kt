package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyMemberRoleResponseDto(
    @SerialName("role")
    val role: Int,
)
