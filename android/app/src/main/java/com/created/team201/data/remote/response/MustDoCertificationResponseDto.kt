package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MustDoCertificationResponseDto(
    @SerialName("me")
    val me: Me,
    @SerialName("others")
    val others: List<Other>
) {
    @Serializable
    data class Me(
        @SerialName("id")
        val id: Int,
        @SerialName("isCertified")
        val isCertified: Boolean,
        @SerialName("nickname")
        val nickname: String,
        @SerialName("profileImageUrl")
        val profileImageUrl: String
    )

    @Serializable
    data class Other(
        @SerialName("id")
        val id: Int,
        @SerialName("isCertified")
        val isCertified: Boolean,
        @SerialName("nickname")
        val nickname: String,
        @SerialName("profileImageUrl")
        val profileImageUrl: String
    )
}
