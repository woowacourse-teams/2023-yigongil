package com.created.team201.data.mapper

import com.created.domain.model.UserStudy
import com.created.team201.data.remote.response.HomeResponseDto
import com.created.team201.presentation.home.uiState.UserStudyUiState


fun HomeResponseDto.toUserStudy(): List<UserStudy> = this.studies.map {
    UserStudy(
        isMaster = it.isMaster,
        grassCount = it.grassCount,
        id = it.id,
        leftDays = it.leftDays,
        name = it.name,
        todoContent = it.todoContent
    )
}

fun List<UserStudy>.toUserStudyUiState(): List<UserStudyUiState> =
    this.map {
        UserStudyUiState(
            studyId = it.id,
            isMaster = it.isMaster,
            studyName = it.name,
            dDay = it.leftDays,
            mustDo = it.todoContent,
            grassSeed = it.grassCount
        )
    }