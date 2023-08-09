package com.created.team201.presentation.updateStudy.model

data class CreateStudyUiModel(
    val name: String,
    val peopleCount: Int,
    val startDate: String,
    val period: Int,
    val cycle: PeriodUiModel,
    val introduction: String,
)
