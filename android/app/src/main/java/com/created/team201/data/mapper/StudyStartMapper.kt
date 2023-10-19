package com.created.team201.data.mapper

import com.created.domain.model.StudyStart
import com.created.team201.data.remote.request.StudyStartRequestDto

fun StudyStart.toRequestDto(): StudyStartRequestDto = StudyStartRequestDto(
    meetingDaysOfTheWeek,
)
