package com.created.team201.presentation.studyList.model

import com.created.domain.model.StudySummary

data class StudySummaryUiModel(
    val id: Long,
    val processingStatus: StudyStatus,
    val averageTier: Int,
    val name: String,
    val createdAt: String,
    val minimumWeeks: Int,
    val meetingDaysCountPerWeek: Int,
    val numberOfCurrentMembers: Int,
    val numberOfMaximumMembers: Int,
) {

    companion object {
        fun StudySummary.toUiModel(): StudySummaryUiModel =
            StudySummaryUiModel(
                id,
                StudyStatus.valueOf(processingStatus),
                averageTier,
                name,
                createdAt,
                minimumWeeks,
                meetingDaysCountPerWeek,
                numberOfCurrentMembers,
                numberOfMaximumMembers,
            )
    }
}
