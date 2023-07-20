package com.created.team201.presentation.studyManage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.created.domain.Period
import com.created.domain.model.StudyManage
import com.created.domain.repository.StudyManageRepository
import com.created.team201.data.datasource.remote.StudyManageDataSourceImpl
import com.created.team201.data.remote.NetworkServiceModule
import com.created.team201.data.repository.StudyManageRepositoryImpl
import com.created.team201.presentation.studyList.model.PeriodUiModel
import com.created.team201.presentation.studyList.model.StudySummaryUiModel
import com.created.team201.presentation.studyManage.model.OnGoingStudiesUiModel
import com.created.team201.presentation.studyManage.model.OnGoingStudyStatus.OPENED
import com.created.team201.presentation.studyManage.model.OnGoingStudyStatus.PARTICIPATED
import com.created.team201.presentation.studyManage.model.StudyManageUiModel
import kotlinx.coroutines.launch

class StudyManageViewModel(
    private val studyManageRepository: StudyManageRepository,
) : ViewModel() {
    private val studies: MutableLiveData<List<StudyManageUiModel>> = MutableLiveData()
    private var _onGoingStudies: MutableLiveData<List<OnGoingStudiesUiModel>> = MutableLiveData()
    val onGoingStudiesUiModel: LiveData<List<OnGoingStudiesUiModel>>
        get() = _onGoingStudies

    init {
        studies.value = listOf()
        loadStudies()
    }

    private fun loadStudies() {
        viewModelScope.launch {
            kotlin.runCatching {
                studies.value = studyManageRepository.getMyStudies().toUiModel()
            }.onSuccess {
                updateStudies()
            }.onFailure { }
        }
    }

    private fun updateStudies() {
        _onGoingStudies.value = listOf(
            OnGoingStudiesUiModel(
                status = PARTICIPATED,
                studySummariesUiModel = studies.value?.mapNotNull { item ->
                    item.studySummaryUiModel.takeIf { !item.isMaster }
                } ?: listOf(),
            ),
            OnGoingStudiesUiModel(
                status = OPENED,
                studySummariesUiModel = studies.value?.mapNotNull { item ->
                    item.studySummaryUiModel.takeIf { item.isMaster }
                } ?: listOf(),
            ),
        )
    }

    private fun StudyManage.toUiModel(): StudyManageUiModel =
        StudyManageUiModel(
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
            ),
            true,
        )

    private fun List<StudyManage>.toUiModel(): List<StudyManageUiModel> =
        this.map { it.toUiModel() }

    private fun Period.toUiModel(): PeriodUiModel =
        PeriodUiModel(number, unit)

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                StudyManageViewModel(
                    StudyManageRepositoryImpl(
                        StudyManageDataSourceImpl(
                            NetworkServiceModule.studyManageService,
                        ),
                    ),
                )
            }
        }
    }
}
