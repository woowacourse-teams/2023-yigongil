package com.created.team201.presentation.studyList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.created.domain.Page
import com.created.domain.Period
import com.created.domain.StudySummary
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

    init {
        _studySummaries.value = listOf()
        loadPage()
    }

    fun loadPage() {
        viewModelScope.launch {
            runCatching {
                studyListRepository.getStudyList(page.index)
            }.onSuccess {
                val newItems = _studySummaries.value?.toMutableList()
                newItems?.addAll(it.toUiModel())
                _studySummaries.value = newItems?.toList()
                page++
            }.onFailure {
            }
        }
    }

    fun refreshPage() {
        page = Page(0)
        loadPage()
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
        PeriodUiModel(number, unit)

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
