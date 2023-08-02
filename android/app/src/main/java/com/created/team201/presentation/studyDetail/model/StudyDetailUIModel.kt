package com.created.team201.presentation.studyDetail.model

data class StudyDetailUIModel(
    val isMaster: Boolean,
    val title: String,
    val introduction: String,
    val peopleCount: Int,
    val startDate: String,
    val period: String,
    val cycle: String,
    val applicantCount: Int,
    val studyMembers: List<StudyMemberUIModel>,
)
