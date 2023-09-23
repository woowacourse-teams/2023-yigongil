package com.created.team201.presentation.studyThread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.domain.repository.ThreadRepository
import com.created.team201.presentation.studyThread.uiState.FeedsUiState
import com.created.team201.presentation.studyThread.uiState.MustDoCertificationUiState
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

    private val studyId: MutableStateFlow<Long> = MutableStateFlow(0)

    val message: MutableStateFlow<String> = MutableStateFlow("")

    private val _mustDoUiState: MutableStateFlow<MustDoCertificationUiState> =
        MutableStateFlow(MustDoCertificationUiState.Loading)
    val mustDoUiState: StateFlow<MustDoCertificationUiState> get() = _mustDoUiState

    private val _feedsUiState: MutableStateFlow<FeedsUiState> =
        MutableStateFlow(FeedsUiState.Loading)
    val feedsUiState: StateFlow<FeedsUiState> get() = _feedsUiState

    fun updateStudyId(id: Long) {
        studyId.value = id
    }

    fun updateMustDoCertification() {
        viewModelScope.launch {
            threadRepository.getMustDo(studyId.value).collectLatest {
                _mustDoUiState.value = MustDoCertificationUiState.Success(it)
            }
        }
    }

    fun sendFeeds() {
        viewModelScope.launch {
            threadRepository.postFeeds(studyId.value, message.value, null)
                .onSuccess { updateFeeds() }
                .onFailure { }
        }
    }

    fun updateFeeds() {
        viewModelScope.launch {
            threadRepository.getFeeds(studyId.value).collectLatest {
                _feedsUiState.value = FeedsUiState.Success(it)
            }
        }
    }
}