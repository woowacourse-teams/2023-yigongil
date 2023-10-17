package com.created.team201.presentation.studyThread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.created.domain.model.Feeds
import com.created.domain.model.MustDoCertification
import com.created.domain.model.Role
import com.created.domain.repository.StudyDetailRepository
import com.created.domain.repository.ThreadRepository
import com.created.team201.presentation.studyDetail.model.StudyDetailUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThreadViewModel @Inject constructor(
    private val threadRepository: ThreadRepository,
    private val detailRepository: StudyDetailRepository,
) : ViewModel() {

    private var studyId: Long = 0

    private val _uiState: MutableStateFlow<ThreadUiState> = MutableStateFlow(ThreadUiState.Loading)
    val uiState: StateFlow<ThreadUiState> get() = _uiState.asStateFlow()

    lateinit var studyDetail: StudyDetailUIModel

    fun initStudyThread(studyId: Long) {
        this.studyId = studyId
        fetchStudyDetail()
        updateMustDoCertification()
        updateFeeds()
    }

    fun dispatchFeed(message: String) {
        viewModelScope.launch {
            runCatching { threadRepository.updateFeeds(studyId, message, null) }
                .onFailure {
                }
        }
    }

    private fun updateMustDoCertification() {
        viewModelScope.launch {
            runCatching { threadRepository.getMustDoCertification(studyId) }
                .onSuccess {
                    updateMustDoCertificationInState(it)
                }
        }
    }

    private fun updateMustDoCertificationInState(certification: MustDoCertification) {
        if (uiState.value is ThreadUiState.Success) {
            _uiState.value =
                (uiState.value as ThreadUiState.Success).copy(
                    studyName = certification.studyName,
                    mustDo = listOf(certification.me) + certification.others,
                )
        } else {
            _uiState.value = ThreadUiState.Success(
                studyName = certification.studyName,
                mustDo = listOf(certification.me) + certification.others,
            )
        }
    }

    private fun updateFeeds() {
        viewModelScope.launch {
            threadRepository.getFeeds(studyId).collectLatest {
                updateFeedsInState(it)
            }
        }
    }

    private fun updateFeedsInState(feeds: List<Feeds>) {
        if (uiState.value is ThreadUiState.Success) {
            _uiState.value =
                (uiState.value as ThreadUiState.Success).copy(
                    feeds = feeds,
                )
        } else {
            _uiState.value = ThreadUiState.Success(
                feeds = feeds,
            )
        }
    }

    private fun fetchStudyDetail() {
        viewModelScope.launch {
            runCatching {
                detailRepository.getStudyDetail(studyId)
            }.onSuccess {
                studyDetail = StudyDetailUIModel.createFromStudyDetailRole(it, Role.NOTHING)
            }
        }
    }
}
