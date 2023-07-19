package com.created.team201.presentation.studyManage.model

import com.created.team201.presentation.studyList.model.StudySummaryUiModel

data class OnGoingStudiesUiModel(
    val status: OnGoingStudyStatus,
    val studySummariesUiModel: List<StudySummaryUiModel>,
)
