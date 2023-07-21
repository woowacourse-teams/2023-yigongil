package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserStudiesResponseDto(
    @SerialName("myId")
    val myId: Int,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("studies")
    val studies: List<StudyResponseDto>,
)
