package com.created.domain.model

data class StudyDetail(
    val id: Long,
    val processingStatus: Int,
    val name: String,
    val numberOfCurrentMembers: Int,
    val numberOfMaximumMembers: Int,
    val studyMasterId: Long,
    val role: Role,
    val startAt: String,
    val totalRoundCount: Int,
    val periodOfRound: String,
    val currentRound: Int,
    val introduction: String,
    val members: List<Member>,
    val rounds: List<Round>,
) {
    companion object {
        private const val START_MEMBER_CONDITION = 2

        fun canStartStudy(numberOfCurrentMembers: Int): Boolean =
            numberOfCurrentMembers >= START_MEMBER_CONDITION
    }
}
