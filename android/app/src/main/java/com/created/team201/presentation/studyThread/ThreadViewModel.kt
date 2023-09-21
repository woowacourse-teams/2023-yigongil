package com.created.team201.presentation.studyThread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.domain.repository.ThreadRepository
import com.created.team201.presentation.studyThread.uiState.ThreadActivityUiState
import com.created.team201.presentation.studyThread.uiState.ThreadActivityUiState.Loading
import com.created.team201.presentation.studyThread.uiState.ThreadActivityUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThreadViewModel @Inject constructor(
    private val threadRepository: ThreadRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<ThreadActivityUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<ThreadActivityUiState> get() = _uiState

    fun initMustDoCertification(studyId: Long) {
        viewModelScope.launch {
            threadRepository.getMustDo(studyId).collectLatest {
                _uiState.value = Success(it)
            }
        }
    }
}