package com.created.team201.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportRequestDTO(
    @SerialName("reportedMemberId")
    val reportedMemberId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("problemOccuredAt")
    val problemOccuredAt: String,
    @SerialName("content")
    val content: String,
)
