package com.created.team201.presentation.studyList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.created.domain.model.Page
import com.created.domain.model.StudySummary
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
    private val _studySummaries = MutableLiveData<List<StudySummaryUiModel>>()
    val studySummaries: LiveData<List<StudySummaryUiModel>>
        get() = _studySummaries
    private val _scrollState = MutableLiveData(true)
    val scrollState: LiveData<Boolean>
        get() = _scrollState
    private val _loadingState = MutableLiveData(false)
    val loadingState: LiveData<Boolean>
        get() = _loadingState
    private val isSearchMode: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)
    private val _isNotFoundStudies: NonNullMutableLiveData<Boolean> = NonNullMutableLiveData(false)
    val isNotFoundStudies: NonNullLiveData<Boolean> get() = _isNotFoundStudies
    private var recentSearchWord: String = ""

    init {
        _studySummaries.value = listOf()
    }

    fun initPage() {
        refreshPage()
    }

    private fun loadPage() {
        viewModelScope.launch {
            runCatching {
                studyListRepository.getStudyList(page.index)
            }.onSuccess {
                if (it.isNotEmpty()) {
                    _isNotFoundStudies.value = false
                    val newItems = _studySummaries.value?.toMutableList()
                    newItems?.addAll(it.toUiModel())
                    _studySummaries.value = newItems?.toList()
                    page++
                    return@launch
                }
            }.onFailure {
            }
        }
    }

    fun loadSearchedPage(searchWord: String) {
        viewModelScope.launch {
            runCatching {
                recentSearchWord = searchWord
                studyListRepository.getSearchedStudyList(searchWord, 0)
            }.onSuccess {
                if (it.isNotEmpty()) {
                    _isNotFoundStudies.value = false
                    _studySummaries.value = mutableListOf()
                    val newItems = _studySummaries.value?.toMutableList()
                    newItems?.addAll(it.toUiModel())
                    _studySummaries.value = newItems?.toList()
                    page++
                    return@launch
                }
                setNotFoundStudies()
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
        if (isSearchMode.value) {
            loadSearchedPage(recentSearchWord)
            return
        }
        loadPage()
    }

    fun loadNextPage(searchWord: String = "") {
        if (page == Page(0)) {
            return
        }
        _loadingState.value = true

        if (isSearchMode.value) {
            loadSearchedPage(searchWord)
            _loadingState.value = false
            return
        }

        loadPage()
        _loadingState.value = false
    }

    fun updateScrollState(state: Int) {
        _scrollState.value = when (state) {
            RecyclerView.SCROLL_STATE_IDLE -> true
            else -> false
        }
    }

    fun changeSearchMode(mode: Boolean) {
        isSearchMode.value = mode
    }

    private fun List<StudySummary>.toUiModel(): List<StudySummaryUiModel> =
        this.map { it.toUiModel() }
}
