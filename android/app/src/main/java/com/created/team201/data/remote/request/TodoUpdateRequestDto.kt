package com.created.team201.data.remote.request

import kotlinx.serialization.SerialName

data class TodoUpdateRequestDto(
    @SerialName("isDone")
    val isDone: Boolean,
    @SerialName("content")
    val content: String,
)
