package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NicknameResponseDto(
    @SerialName("exists")
    val exists: Boolean,
)
