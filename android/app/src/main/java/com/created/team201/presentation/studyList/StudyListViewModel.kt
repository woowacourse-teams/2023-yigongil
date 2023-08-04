package com.created.team201.presentation.studyList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.RecyclerView
import com.created.domain.model.Page
import com.created.domain.model.Period
import com.created.domain.model.StudySummary
import com.created.domain.repository.StudyListRepository
import com.created.team201.data.datasource.remote.StudyListDataSourceImpl
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.StudyListRepositoryImpl
import com.created.team201.presentation.studyList.model.PeriodUiModel
import com.created.team201.presentation.studyList.model.StudySummaryUiModel
import kotlinx.coroutines.launch

class StudyListViewModel(
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
                    val newItems = _studySummaries.value?.toMutableList()
                    newItems?.addAll(it.toUiModel())
                    _studySummaries.value = newItems?.toList()
                    page++
                }
            }.onFailure {
            }
        }
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

    private fun List<StudySummary>.toUiModel(): List<StudySummaryUiModel> =
        this.map { it.toUiModel() }

    private fun StudySummary.toUiModel(): StudySummaryUiModel =
        StudySummaryUiModel(
            id,
            processingStatus,
            tier,
            title,
            date,
            totalRound,
            period.toUiModel(),
            currentMember,
            maximumMember,
        )

    private fun Period.toUiModel(): PeriodUiModel =
        PeriodUiModel(date, unit)

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                StudyListViewModel(
                    StudyListRepositoryImpl(
                        StudyListDataSourceImpl(
                            NetworkServiceModule.studyListService,
                        ),
                    ),
                )
            }
        }
    }
}
