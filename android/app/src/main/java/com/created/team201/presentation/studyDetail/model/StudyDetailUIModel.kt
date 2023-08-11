package com.created.team201.presentation.studyDetail.model

import com.created.domain.model.Role

data class StudyDetailUIModel(
    val studyMasterId: Long,
    val isMaster: Boolean,
    val title: String,
    val introduction: String,
    val peopleCount: Int,
    val role: Role,
    val startDate: String,
    val period: String,
    val cycle: String,
    val memberCount: Int,
    val canStartStudy: Boolean,
    val studyMembers: List<StudyMemberUIModel>,
) {
    companion object {
        val INVALID_STUDY_DETAIL = StudyDetailUIModel(
            studyMasterId = 0,
            isMaster = false,
            title = "",
            introduction = "",
            peopleCount = 0,
            role = Role.NOTHING,
            startDate = "",
            period = "",
            cycle = "",
            memberCount = 0,
            canStartStudy = false,
            studyMembers = listOf(),
        )
    }
}
