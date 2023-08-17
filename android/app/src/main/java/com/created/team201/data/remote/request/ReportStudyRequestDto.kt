package com.created.team201.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportStudyRequestDto(
    @SerialName("reportedStudyId")
    val reportedStudyId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("problemOccurredAt")
    val problemOccurredAt: String,
    @SerialName("content")
    val content: String,
)
