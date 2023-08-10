package com.created.domain.model

data class Report(
    val targetId: Long,
    val category: Int,
    val title: String,
    val problemOccurredAt: String,
    val content: String,
)
