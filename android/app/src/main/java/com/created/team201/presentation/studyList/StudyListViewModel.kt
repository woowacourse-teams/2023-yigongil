package com.created.team201.presentation.studyList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.created.domain.model.Page
import com.created.domain.repository.StudyListRepository
import com.created.team201.presentation.studyList.model.StudySummaryUiModel
import com.created.team201.presentation.studyList.model.StudySummaryUiModel.Companion.toUiModel
import com.created.team201.util.NonNullLiveData
import com.created.team201.util.NonNullMutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudyListViewModel @Inject constructor(
    private val studyListRepository: StudyListRepository,
) : ViewModel() {

    private var page: Page = Page()

    private val _studySummaries: MutableLiveData<List<StudySummaryUiModel>> =
        MutableLiveData(listOf())
    val studySummaries: LiveData<List<StudySummaryUiModel>>
        get() = _studySummaries

    private val _scrollState = MutableLiveData(true)
    val scrollState: LiveData<Boolean>
        get() = _scrollState

    private val _loadingState = MutableLiveData(false)
    val loadingState: LiveData<Boolean>
        get() = _loadingState

    private val _isNotFoundStudies: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)
    val isNotFoundStudies: NonNullLiveData<Boolean>
        get() = _isNotFoundStudies

    private var recentSearchWord: String = ""
    private var filterStatus: String? = null

    fun initPage() {
        refreshPage()
    }

    fun loadPage() {
        viewModelScope.launch {
            runCatching {
                studyListRepository.getStudyList(filterStatus, page.index, recentSearchWord)
            }.onSuccess {
                if (it.isNotEmpty()) {
                    _isNotFoundStudies.value = false
                    val newItems = _studySummaries.value?.toMutableList()
                    _studySummaries.value = mutableListOf()
                    newItems?.addAll(it.toUiModel())
                    _studySummaries.value = newItems?.toList()
                    page++
                    return@launch
                }
                if (page != Page(0)) {
                    setNotFoundStudies()
                }
            }.onFailure {
            }
        }
    }

    private fun setNotFoundStudies() {
        _isNotFoundStudies.value = true
        _studySummaries.value = emptyList()
    }

    fun refreshPage() {
        page = Page(0)
        _studySummaries.value = listOf()
        loadPage()
    }

    fun loadNextPage() {
        if (page == Page(0)) {
            return
        }
        _loadingState.value = true
        loadPage()
        _loadingState.value = false
    }

    fun updateScrollState(state: Int) {
        _scrollState.value = when (state) {
            RecyclerView.SCROLL_STATE_IDLE -> true
            else -> false
        }
    }

    fun changeSearchWord(searchWord: String) {
        recentSearchWord = searchWord
    }

    fun loadFilteredPage(status: String?) {
        filterStatus = status
        refreshPage()
    }

    fun loadAppliedPage(isGuest: Boolean) {
        if (isGuest) {
            // 로그인이 필요한 페이지 입니다.
            _studySummaries.value = emptyList()
            return
        }
        viewModelScope.launch {
            runCatching {
                studyListRepository.getAppliedStudyList(recentSearchWord)
            }.onSuccess {
                if (it.isNotEmpty()) {
                    _studySummaries.value = it.toUiModel()
                    return@launch
                }
                setNotFoundStudies()
            }
        }
    }
}
