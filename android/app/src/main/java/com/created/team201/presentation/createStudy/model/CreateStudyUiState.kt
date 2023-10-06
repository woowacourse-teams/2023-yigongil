package com.created.team201.presentation.createStudy.model

sealed interface CreateStudyUiState {
    object Success : CreateStudyUiState

    object Fail : CreateStudyUiState

    object Idle : CreateStudyUiState
}
