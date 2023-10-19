package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MustDoCertificationResponseDto(
    @SerialName("studyName")
    val studyName: String,
    @SerialName("upcomingRound")
    val upcomingRound: UpcomingRound,
    @SerialName("me")
    val me: User,
    @SerialName("others")
    val others: List<User>,
) {
    @Serializable
    data class User(
        @SerialName("id")
        val id: Int,
        @SerialName("nickname")
        val nickname: String,
        @SerialName("profileImageUrl")
        val profileImageUrl: String,
        @SerialName("isCertified")
        val isCertified: Boolean,
    )

    @Serializable
    data class UpcomingRound(
        @SerialName("id")
        val id: Int,
        @SerialName("weekNumber")
        val weekNumber: Int,
    )
}
