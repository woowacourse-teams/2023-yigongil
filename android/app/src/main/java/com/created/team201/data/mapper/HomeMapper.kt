package com.created.team201.data.mapper

import com.created.domain.model.UserStudy
import com.created.team201.data.remote.response.HomeResponseDto
import com.created.team201.presentation.home.uiState.UserStudyUiState


fun HomeResponseDto.toUserStudy(): List<UserStudy> = this.studies.map { study ->
    UserStudy(
        isMaster = study.isMaster,
        grassCount = study.grassCount,
        id = study.id,
        leftDays = study.leftDays,
        name = study.name,
        todoContent = study.todoContent
    )
}

fun List<UserStudy>.toUserStudyUiState(): List<UserStudyUiState> =
    this.map { study ->
        UserStudyUiState(
            studyId = study.id,
            isMaster = study.isMaster,
            studyName = study.name,
            dDay = study.leftDays,
            mustDo = study.todoContent,
            grassSeed = study.grassCount
        )
    }