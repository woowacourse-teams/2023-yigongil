package com.created.team201.presentation.studyDetail.model

import com.created.domain.model.Member

data class StudyMemberUIModel(
    val id: Long,
    val isMaster: Boolean,
    val isApplicant: Boolean,
    val profileImageUrl: String,
    val name: String,
    val successRate: Int,
    val tier: Int,
    val isDeleted: Boolean,
) {
    companion object {

        fun Member.toUiModel(
            studyMasterId: Long,
            isApplicant: Boolean,
        ): StudyMemberUIModel =
            StudyMemberUIModel(
                id = id,
                isMaster = this.id == studyMasterId,
                isApplicant = isApplicant,
                profileImageUrl = this.profileImage,
                name = this.nickname,
                successRate = this.successRate.toInt(),
                tier = this.tier,
                isDeleted = this.isDeleted,
            )
    }
}
