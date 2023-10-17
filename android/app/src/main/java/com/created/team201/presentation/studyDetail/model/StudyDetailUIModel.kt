package com.created.team201.presentation.studyDetail.model

import com.created.domain.model.Role
import com.created.domain.model.StudyDetail
import com.created.team201.presentation.studyDetail.model.StudyMemberUIModel.Companion.toUiModel

data class StudyDetailUIModel(
    val studyMasterId: Long,
    val isMaster: Boolean,
    val name: String,
    val introduction: String,
    val peopleCount: Int,
    val role: Role,
    val minimumWeeks: Int,
    val meetingDaysCountPerWeek: Int,
    val memberCount: Int,
    val canStartStudy: Boolean,
    val studyMembers: List<StudyMemberUIModel>,
) {
    companion object {
        val INVALID_STUDY_DETAIL = StudyDetailUIModel(
            studyMasterId = 0,
            isMaster = false,
            name = "",
            introduction = "",
            peopleCount = 0,
            role = Role.NOTHING,
            minimumWeeks = 0,
            meetingDaysCountPerWeek = 0,
            memberCount = 0,
            canStartStudy = false,
            studyMembers = listOf(),
        )

        fun createFromStudyDetailRole(studyDetail: StudyDetail, role: Role): StudyDetailUIModel =
            StudyDetailUIModel(
                studyMasterId = studyDetail.studyMasterId,
                isMaster = role == Role.MASTER,
                name = studyDetail.name,
                introduction = studyDetail.introduction,
                peopleCount = studyDetail.numberOfMaximumMembers,
                role = role,
                minimumWeeks = studyDetail.minimumWeeks,
                meetingDaysCountPerWeek = studyDetail.meetingDaysCountPerWeek,
                memberCount = studyDetail.numberOfCurrentMembers,
                canStartStudy = StudyDetail.canStartStudy(studyDetail.numberOfCurrentMembers),
                studyMembers = studyDetail.members.map {
                    it.toUiModel(
                        studyDetail.studyMasterId,
                        isApplicant = false,
                    )
                },
            )
    }
}
