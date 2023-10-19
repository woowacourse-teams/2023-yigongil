package com.created.domain.model

data class MustDoCertification(
    val studyName: String,
    val upcomingRound: UpcomingRound,
    val me: MustDo,
    val others: List<MustDo>,
) {
    data class UpcomingRound(
        val id: Int,
        val weekNumber: Int,
    )
}
