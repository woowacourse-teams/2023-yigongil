package com.created.team201.data.mapper

import com.created.domain.model.CreateStudy
import com.created.team201.data.remote.request.CreateStudyRequestDto

fun CreateStudy.toRequestDto(): CreateStudyRequestDto =
    CreateStudyRequestDto(
        name = name,
        peopleCount = peopleCount,
        studyDate = studyDate,
        numberOfStudyPerWeek = numberOfStudyPerWeek,
        introduction = introduction,
    )
