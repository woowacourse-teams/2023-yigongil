package com.created.team201.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RenewedAccessTokenRequestDTO(
    @SerialName("refreshToken")
    val refreshToken: String,
)
