package com.created.team201.presentation.studyThread.uiState

import com.created.domain.model.MustDo

sealed interface ThreadActivityUiState {
        object Loading : ThreadActivityUiState
        data class Success(val mustDo: List<MustDo>) : ThreadActivityUiState
    }