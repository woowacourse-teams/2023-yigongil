package com.created.team201.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportUserRequestDto(
    @SerialName("reportedMemberId")
    val reportedMemberId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("problemOccuredAt")
    val problemOccurredAt: String,
    @SerialName("content")
    val content: String,
)
