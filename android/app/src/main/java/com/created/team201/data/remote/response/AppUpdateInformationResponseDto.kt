package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppUpdateInformationResponseDto(
    @SerialName("shouldUpdate")
    val shouldUpdate: Boolean,
    @SerialName("message")
    val message: String,
)
