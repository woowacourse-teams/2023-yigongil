package com.created.team201.presentation.studyManage.model

import com.created.domain.model.Role
import com.created.team201.presentation.studyList.model.StudySummaryUiModel

data class StudyManageUiModel(
    val role: Role,
    val studySummaryUiModel: StudySummaryUiModel,
)
