package com.created.team201.presentation.createStudy.model

sealed interface CreateStudyUiState {
    class Success(val studyId: Long) : CreateStudyUiState

    object Fail : CreateStudyUiState

    object Idle : CreateStudyUiState
}
