package com.created.team201.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportRequestDTO(
    @SerialName("targetId")
    val targetId: Long,
    @SerialName("category")
    val category: Int,
    @SerialName("title")
    val title: String,
    @SerialName("problemOccurredAt")
    val problemOccurredAt: String,
    @SerialName("content")
    val content: String,
)
