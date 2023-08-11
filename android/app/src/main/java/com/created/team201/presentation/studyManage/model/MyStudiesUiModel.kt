package com.created.team201.presentation.studyManage.model

import com.created.team201.presentation.studyList.model.StudySummaryUiModel

data class MyStudiesUiModel(
    val status: MyStudyStatus,
    val studySummariesUiModel: List<StudySummaryUiModel>,
)
