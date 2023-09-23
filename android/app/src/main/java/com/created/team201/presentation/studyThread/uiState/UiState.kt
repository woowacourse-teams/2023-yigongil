package com.created.team201.presentation.studyThread.uiState

import com.created.domain.model.Feeds
import com.created.domain.model.MustDo

sealed interface MustDoCertificationUiState {
    object Loading : MustDoCertificationUiState
    data class Success(val mustDo: List<MustDo>) : MustDoCertificationUiState
}

sealed interface FeedsUiState {
    object Loading : FeedsUiState
    data class Success(val feeds: List<Feeds>) : FeedsUiState
}