package com.created.team201.presentation.studyThread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    init {
        updateMustDoCertification()
        updateFeeds()
    }

    fun updateStudyId(id: Long) {
        studyId = id
        fetchStudyDetail()
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
                    _uiState.value =
                        ThreadUiState.Success()
                            .copy(studyName = it.studyName, mustDo = listOf(it.me) + it.others)
                }
        }
    }

    private fun updateFeeds() {
        viewModelScope.launch {
            threadRepository.getFeeds(studyId).collectLatest {
                _uiState.value = ThreadUiState.Success().copy(feeds = it)
            }
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
