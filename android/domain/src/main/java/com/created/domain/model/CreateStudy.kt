package com.created.domain.model

data class CreateStudy(
    val name: String,
    val peopleCount: Int,
    val startDate: String,
    val period: Period,
    val cycle: Period,
    val introduction: String,
)
