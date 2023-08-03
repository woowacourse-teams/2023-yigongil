package com.created.team201.presentation.studyDetail.model

data class StudyMemberUIModel(
    val id: Long,
    val isMaster: Boolean,
    val isApplicant: Boolean,
    val profileImageUrl: String,
    val name: String,
    val successRate: Int,
    val tier: Int,
) {
    companion object {
        val DUMMY = StudyMemberUIModel(
            id = 0L,
            isMaster = true,
            isApplicant = false,
            tier = 3,
            name = "bandal",
            successRate = 90,
            profileImageUrl = "https://opgg-com-image.akamaized.net/attach/images/20200321020018.373875.jpg",
        )
    }
}
