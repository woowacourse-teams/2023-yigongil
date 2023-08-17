package com.created.team201.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyProfileRequestDTO(
    @SerialName("nickname")
    val nickname: String,
    @SerialName("introduction")
    val introduction: String,
)
