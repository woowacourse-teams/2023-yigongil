package com.created.team201.presentation.studyThread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.domain.repository.ThreadRepository
import com.created.team201.presentation.studyThread.uiState.ThreadActivityUiState
import com.created.team201.presentation.studyThread.uiState.ThreadActivityUiState.Loading
import com.created.team201.presentation.studyThread.uiState.ThreadActivityUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ThreadViewModel @Inject constructor(
    private val threadRepository: ThreadRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<ThreadActivityUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<ThreadActivityUiState> get() = _uiState

    fun initMustDoCertification(studyId: Long) {
        _uiState.value = threadRepository.getMustDo(studyId)
            .map { Success(it) }
            .stateIn(
                scope = viewModelScope,
                initialValue = Loading,
                started = SharingStarted.WhileSubscribed(5_000),
            ).value
    }
}