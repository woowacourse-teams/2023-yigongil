package com.created.team201.presentation.studyThread

import com.created.domain.model.Feeds
import com.created.domain.model.MustDo

sealed interface ThreadUiState {
    data class Success(
        val studyName: String = "",
        val feeds: List<Feeds> = emptyList(),
        val mustDo: List<MustDo> = emptyList(),
    ) : ThreadUiState

    object Loading : ThreadUiState
}
