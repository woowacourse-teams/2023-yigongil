package com.created.team201.presentation.studyManage.model

import com.created.team201.presentation.studyList.model.StudySummaryUiModel

data class StudyManageUiModel(
    val studySummaryUiModel: StudySummaryUiModel,
    val isMaster: Boolean,
)
