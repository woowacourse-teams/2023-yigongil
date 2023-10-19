package com.created.domain.model

data class CreateStudy(
    val name: String,
    val peopleCount: Int,
    val studyDate: Int,
    val numberOfStudyPerWeek: Int,
    val introduction: String,
)
